package dev.trelawnm.tictactoe.domain.model;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public record Game(UUID uuid, Board board) {
    public List<Move> getAvailableMoves() {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.board.grid()[i][j] == 0) moves.add(new Move(i, j));
            }
        }
        return moves;
    }

    public Board placeMark(Move mv, int player) {
        Board newBoard = deepCopy(this.board);
        newBoard.grid()[mv.row()][mv.col()] = player;
        return newBoard;
    }

    private Board deepCopy(Board board) {
        int[][] copy = new int[3][3];

        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++ )
                copy[r][c] = this.board.grid()[r][c];
        
        return new Board(copy);
    }

    public int getWinner() {
        int[][] grid = this.board.grid();

        for (int row = 0; row < 3; row++) {
            if (grid[row][0] != 0 && grid[row][0] == grid[row][1] && grid[row][1] == grid[row][2])
                return grid[row][0];
        }

        for (int col = 0; col < 3; col++) {
            if (grid[0][col] != 0 && grid[0][col] == grid[1][col] && grid[1][col] == grid[2][col])
                return grid[0][col];
        }

        if (grid[0][0] != 0 && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2])
            return grid[0][0];

        if (grid[0][2] != 0 && grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0])
            return grid[0][2];

        return 0;
    }
}