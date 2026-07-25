package dev.trelawnm.tictactoe.domain.model;

public record Board(int[][] grid) {

    public boolean isFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (this.grid[i][j] == 0) return false;
        
        return true;
    }
}