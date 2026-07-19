package dev.trelawnm.tictactoe.domain.model;

import java.util.ArrayList;
import java.util.List;

import dev.trelawnm.tictactoe.domain.exceptions.IllegalMoveException;

/**
 * A Value Object
 */
public record Board(int[][] grid) {
    private static final int EMPTY = 0;

    public Board {
        grid = deepCopy(grid);
    }

    public static Board empty() {
        return new Board(new int[3][3]);
    }

    public Player getWinner() {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++) {
            if (grid[row][0] == grid[row][1] && grid[row][1] == grid[row][2]) {
                return Player.fromSymbol(grid[row][0]);
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++) {
            if (grid[0][col] == grid[1][col] && grid[1][col] == grid[2][col]) {
                return Player.fromSymbol(grid[0][col]);
            }
        }

        // Checking for Diagonals for X or O victory.
        if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) {
            return Player.fromSymbol(grid[1][1]);
        }
        if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]) {
            return Player.fromSymbol(grid[1][1]);
        }

        return null;
    }

    public boolean isFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (grid[i][j] == EMPTY) return false;
        
        return true;
    }

    public List<Move> getAvailableMoves() {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == EMPTY) moves.add(new Move(i, j));
            }
        }
        return moves;
    }

    // МУТАЦИЯ (создание нового экземпляра)
    public Board placeMark(Move mv, Player player) throws IllegalMoveException {
        int r = mv.row();
        int c = mv.col();
        if (grid[r][c] != EMPTY) throw new IllegalMoveException("Cell occupied");
        int[][] newGrid = deepCopy(grid);
        newGrid[r][c] = player.getSymbol();
        return new Board(newGrid);
    }

    private int[][] deepCopy(int[][] src) {
        int row = src.length;
        int col = src[0].length;

        int[][] copy = new int[row][col];

        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++ )
                copy[r][c] = src[r][c];
        
        return copy;
    }
}