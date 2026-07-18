package dev.trelawnm.tictactoe.domain.model;

import java.util.UUID;

public class CurrentGame {
    private GameBoard board;
    public final UUID uuid;

    // store the current player

    public CurrentGame() {
        this.board = new GameBoard();
        this.uuid = UUID.randomUUID();
    }

    public GameBoard getBoard() {
        return this.board; // TODO: make method .copy() in GameBoard class and call it
    }
}