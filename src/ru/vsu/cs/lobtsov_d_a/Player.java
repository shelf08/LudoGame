package ru.vsu.cs.lobtsov_d_a;
class Player {
//тут игрок, по дефолту
    private String color;
    private int numberRolled;
    private boolean hasRolledSix;
    private Piece[] piece;

    Player(String color) {

        this.color = color;

        piece = new Piece[4];

        for(int i=0; i<4; i++)
            piece[i] = new Piece(i, color);

    }

    void rollDice() {

        // nextInt() gives an int from 0 to 5, adding 1
        // to make it in the range of 1-6
        numberRolled = new Dice().roll();

        if(numberRolled==6)
            hasRolledSix = true;
        else hasRolledSix = false;

    }

    Piece getPiece(int pieceNumber) {
        return piece[pieceNumber];
    }

    String getColor() {
        return color;
    }

    int getNumberRolled() {
        return numberRolled;
    }

    boolean hasRolledSix() {
        return hasRolledSix;
    }

    @Override
    public String toString() {
        return " Игрок " + color;
    }

    boolean hasWon() {

        // if any piece is not completed,
        // will return false
        for(int i=0; i<4; i++)
            if(!piece[i].isCompleted())
                return false;

        // otherwise true - hooray!
        return true;

    }

}
