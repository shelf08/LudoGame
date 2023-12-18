package ru.vsu.cs.lobtsov_d_a;

class Piece {
//класс с пешками(2-4)
    private int pieceNumber;
    private int xCoord;
    private int yCoord;
    private String color;
    private boolean takenOut;
    private boolean completed;

    Piece(int pieceNumber, String color) {

        this.pieceNumber = pieceNumber+1;
        this.color = color;

    }

    String getColor() {
        return color;
    }

    int getPieceNumber() {
        return pieceNumber;
    }

    int getX() {
        return xCoord;
    }

    int getY() {
        return yCoord;
    }

    void setX(int x) {
        xCoord = x;
    }

    void setY(int y) {
        yCoord = y;
    }

    void setTakenOut(boolean mode) {
        takenOut = mode;
    }

    boolean isTakenOut() {
        return takenOut;
    }

    void setCompleted(boolean mode) {
        completed = mode;
    }

    boolean isCompleted() {
        return completed;
    }

}
