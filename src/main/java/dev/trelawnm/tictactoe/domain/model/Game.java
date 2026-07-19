package dev.trelawnm.tictactoe.domain.model;

import java.util.UUID;

/**
 * An Entity
 */
public class Game { // Entity
    private Board board;
    public final UUID uuid;

    // store: current player, 


    public Game() {
        this.board = Board.empty();
        this.uuid = UUID.randomUUID(); // TODO: will be given to the constructor from service / factoria
    }

    public Board getBoard() {
        return this.board; // TODO: make method .copy() in GameBoard class and call it
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}