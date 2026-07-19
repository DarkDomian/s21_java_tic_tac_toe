package dev.trelawnm.tictactoe.domain.model;

public record Move(int row, int col) {
    public Move {
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            throw new IllegalArgumentException("Coordinates must be 0-2");
        }
    }
}