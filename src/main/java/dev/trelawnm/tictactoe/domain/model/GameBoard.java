package dev.trelawnm.tictactoe.domain.model;

public class GameBoard {
    public static final EMPTY = 0;
    public static final X = 1;
    public static final O = -1;

    private int[][] field;
    private int freeCells = 9;

    public GameBoard() {
        // TODO: initialize an empty field filled with '-1'... probably initialized with '0'. fine for getCell
        this.field = new int[3][3];
    }

    public int[][] getBoard() {
        // return an pointer to the fild, that means user will acces the private field
        // TODO: return an copy of the field
        return this.field;
    }

    public int getCell(int x, int y) {
        // TODO: should protect array overflow by check the x and y in [0,1,2]
        return this.field[x][y];
    }

    public boolean cellIsEmpty(int x, int y) {
        if (getCell(x, y) == 0) {
            return true;
        } else 
            return false;
    }

    public void setCell(int x, int y, int value) throws Exception {
        // validate x, y in [0,1,2] - may be write reusable validation method. secure
        if (cellIsEmpty(x, y)) {
            this.field[x][y] = value;
            --this.freeCells;
        }
        else
            throw new Exception("kind of exception here"); // or handle somehow in there
            // possible to return true / false on success / fail 
    }

    public boolean isFull() {
        if (this.freeCells == 0) 
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return String.format(
            "Board = {%d, %d, %d, %d, %d, %d, %d, %d, %d}, free cells = %d",
            this.field[0][0], this.field[0][1], this.field[0][2],
            this.field[1][0], this.field[1][1], this.field[1][2],
            this.field[2][0], this.field[2][1], this.field[2][2],
            this.freeCells
            );
    }
}