package ru.vsu.cs.lobtsov_d_a;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LudoGameHandler {

    private static String[] colorList = { "Зеленый", "Желтый", "Красный", "Синий" };
    private static List<Player> playerList = new ArrayList<Player>();
    private static Player currentPlayer;
    private static LudoBoard ludoBoard;

    public static void main(String[] args) {

        /*
         *
         *  initialize game by asking for the number of players
         *
         */
        System.out.println("Выберите количество игроков 2-4.");
        boolean inputCorrect = false;
        Scanner scanner = null;

        while(!inputCorrect) {

            scanner = new Scanner(System.in);
            int number = 0;
            try {
                number = scanner.nextInt();
            } catch(Exception e) {
                //ignore
            }

            if(number>1 && number<5) {

                inputCorrect = true;

                for(int i=0; i<number; i++)
                    playerList.add(new Player(colorList[i]));

            } else {

                System.out.println("Вы указали не то число, введите 2-4.");

            }

        }

        for(int i=0; i<playerList.size(); i++)
            System.out.println(playerList.get(i) + " присоединился к игре.");

        /*
         *
         * roll the dice to determine who goes first
         *
         */

        System.out.println("Игроки должны бросить кости, чтобы " +
                "определить, кто пойдет первым. Чтобы бросить кости, "
                + "введите \"r\".");
        boolean initComplete = false;
        int playerCounter = 0;

        while(!initComplete) {

            Player currentPlayer = playerList.get(playerCounter);
            System.out.println("Очередь" + currentPlayer);

            scanner = new Scanner(System.in);
            String input = "";

            try {
                input = scanner.next();
            } catch(Exception e) {
                // ignore
            }

            if(input.equals("r")) {

                currentPlayer.rollDice();
                System.out.println(currentPlayer + " получает " + currentPlayer.getNumberRolled());

            } else {

                System.out.println("Что-то пошло не так. Введите \"r\".");
                continue;

            }

            if(++playerCounter==playerList.size())
                initComplete = true;

        }

        for(int i=0; i<playerList.size(); i++)
            System.out.println(playerList.get(i) + " получает " + playerList.get(i).getNumberRolled());

        List<Player> highestRollers = determineHighestRoller(playerList);

        /*
         *
         * if there are multiple high rollers, do a loop
         * to end up with just 1 highest roller
         *
         */

        boolean onlyOneHighest = highestRollers.size()==1;

        while(!onlyOneHighest) {

            String playerNames = "";

            for(int i=0; i<highestRollers.size(); i++)
                playerNames += highestRollers.get(i) + ", ";

            System.out.println("Есть несколько самых больших выпавших костей. Игроки " +
                    playerNames + " кидают кости снова. Запомните, чтобы " +
                    " кинуть кости, type in \"r\"");

            boolean rollsComplete = false;
            int rollerCounter = 0;

            while(!rollsComplete) {

                Player currentPlayer = highestRollers.get(rollerCounter);
                System.out.println(currentPlayer + "'s turn.");

                scanner = new Scanner(System.in);
                String input = "";

                try {
                    input = scanner.next();
                } catch(Exception e) {
                    // ignore
                }

                if(input.equals("r")) {

                    currentPlayer.rollDice();
                    System.out.println(currentPlayer + " выпало " + currentPlayer.getNumberRolled());

                } else {

                    System.out.println("Что-то пошло не так. Введите \"r\".");
                    continue;

                }

                if(++rollerCounter==highestRollers.size())
                    rollsComplete = true;

            }

            for(int i=0; i<highestRollers.size(); i++)
                System.out.println(highestRollers.get(i) + " выпало " + highestRollers.get(i).getNumberRolled());

            highestRollers = determineHighestRoller(highestRollers);

            onlyOneHighest = highestRollers.size()==1;

        }

        currentPlayer = highestRollers.get(0);

        System.out.println(currentPlayer + " первым начинает игру.");

        ludoBoard = new LudoBoard();
        for(int i=0; i<playerList.size(); i++)
            ludoBoard.initializePieces(playerList.get(i));

        runGame();

    }

    /*
     *
     * Controls the game sequence
     *
     */

    private static void runGame() {

        Scanner scanner = null;
        boolean gameCompleted = false;

        game: while(!gameCompleted) {

            ludoBoard.printBoard();

            System.out.println("Очередь " + currentPlayer + " Единственное, что ты можешь сделать, это бросить кости - \"r\".");

            boolean rollComplete = false;

            while(!rollComplete) {

                scanner = new Scanner(System.in);
                String input = "";

                try {
                    input = scanner.next();
                } catch(Exception e) {
                    // ignore
                }

                if(input.equals("r")) {

                    currentPlayer.rollDice();
                    rollComplete = true;

                } else {

                    System.out.println("Что-то пошло не так. Введите \"r\".");
                    continue;

                }

            }

            boolean movesArePossible = ludoBoard.movesArePossible(currentPlayer, currentPlayer.getNumberRolled());

            if(!movesArePossible) {

                System.out.println("Нет никаких возможных ходов. Двигаться дальше...");
                setNextPlayer();
                continue;

            }

            System.out.println(currentPlayer + " выпадает " + currentPlayer.getNumberRolled() +
                    ". Варианты:\n" +
                    "\"t (номер пешки)\" без скобок, чтобы вынуть пешку из домашнего круга;\n" +  //Type t 1 to move piece 1 out of home circle , t 2 for piece two and t 3 and so on (t(spacebar)(piece number))
                    "\"m (номер пешки)\" без скобок, чтобы сходить пешкой."); //Type m 1 to move piece 1, m 2 to move piece 2, m 3 and so on (m(spacebar)(piece number))

            boolean turnComplete = false;

            while(!turnComplete) {

                scanner = new Scanner(System.in);

                String command = null;
                boolean commandSuccessful = false;

                try {
                    command = scanner.next();
                } catch(Exception e) {
                    System.out.println("Неправильный ход. Попробуй снова.");
                    continue;
                }

                if(command.equals("t")) {

                    // if did not roll 6, can't take a piece out
                    if(!currentPlayer.hasRolledSix()) {
                        System.out.println("Неправильный ход. Вынимать можно пешку только " +
                                " когда на кубике выпадает 6.");
                        continue;
                    }

                    int pieceNumber = 0;

                    try {
                        pieceNumber = scanner.nextInt() - 1;
                    } catch(Exception e) {
                        System.out.println("Неправильный номер пешки. Попробуй снова.");
                        continue;
                    }

                    if(pieceNumber<0 || pieceNumber>3) {

                        System.out.println("Неправильный номер пешки!");
                        continue;

                    }

                    Piece piece = currentPlayer.getPiece(pieceNumber);

                    commandSuccessful = ludoBoard.takePieceOut(piece);

                } else if(command.equals("m")) {

                    int pieceNumber = 0;

                    try {
                        pieceNumber = scanner.nextInt() - 1;
                    } catch(Exception e) {
                        System.out.println("Неправильный номер пешки. Попробуй снова.");
                        continue;
                    }

                    if(pieceNumber<0 || pieceNumber>3) {

                        System.out.println("Неправильный номер пешки!");
                        continue;

                    }

                    Piece piece = currentPlayer.getPiece(pieceNumber);
                    int squareAmount = currentPlayer.getNumberRolled();

                    commandSuccessful = ludoBoard.movePiece(piece, squareAmount);

                } else {

                    System.out.println("Неправильная команда. Попробуй снова.");
                    continue;

                }

                if(commandSuccessful) {

                    if(currentPlayer.hasWon()) {

                        System.out.println("Поздравляю! " + currentPlayer + " выиграл игру!");
                        break game;

                    }

                    if(currentPlayer.hasRolledSix()) {
                        System.out.println("Игроку" + currentPlayer + "выпала 6, это означает, что " +
                                "ему дается еще один ход. Бросьте кости.");
                        continue game;
                    }

                    setNextPlayer();
                    turnComplete = true;

                } else System.out.println("Перемещение не может быть завершено. Попробуйте что-нибудь еще.");

            }

        }

        scanner.close();

    }

    /*
     *
     * Return type is list in case there are multiple
     * high-rollers.
     *
     */
    private static List<Player> determineHighestRoller(List<Player> playersWhoRolled) {

        List<Player> highRollers = new ArrayList<Player>();
        Player highestRoller = playersWhoRolled.get(0);
        boolean complete = false;
        int playerCounter = 1;

        while(!complete) {

            Player nextPlayer = playersWhoRolled.get(playerCounter++);

            int currentHighest = highestRoller.getNumberRolled();
            int nextPlayersRoll = nextPlayer.getNumberRolled();

            if(currentHighest>nextPlayersRoll) {

                // do nothing, highestRoller already points
                // to the highest rolling player

            } else if(currentHighest<nextPlayersRoll) {

                // assign next player as the highest roller
                highestRoller = nextPlayer;

                // clear the multiple roller list
                // because nextPlayer has rolled
                // more than both previous players
                highRollers.clear();

            } else if(currentHighest==nextPlayersRoll) {

                // might already contain if there are 3
                // high rollers
                if(!highRollers.contains(highestRoller))
                    highRollers.add(highestRoller);
                if(!highRollers.contains(nextPlayer))
                    highRollers.add(nextPlayer);

            }

            if(playerCounter==playersWhoRolled.size())
                complete = true;

        }

        if(highRollers.size()==0)
            highRollers.add(highestRoller);

        return highRollers;

    }

    /*
     *
     * Sets the next player as current
     * in a circular queue fashion
     *
     */
    private static void setNextPlayer() {

        int nextIndex = playerList.indexOf(currentPlayer) + 1;

        if(nextIndex==playerList.size())
            nextIndex = 0;

        currentPlayer = playerList.get(nextIndex);

    }

}
