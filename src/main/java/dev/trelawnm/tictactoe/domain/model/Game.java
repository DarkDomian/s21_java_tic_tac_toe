package dev.trelawnm.tictactoe.domain.model;

import java.util.UUID;

import dev.trelawnm.tictactoe.exceptions.IllegalMoveException;

/**
 * An Entity
 */
public class Game { // Entity
    private Board board;
    private Player currentPlayer;
    private final UUID uuid;

    public Game(UUID id) {
        this.board = Board.empty();
        this.uuid = id;
        this.currentPlayer = Player.O;
    }

    public Board getBoard() {
        return new Board(board.grid());
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public UUID getId() {
        return new UUID(uuid);
    }

    public void makeMove(Move move) {
        try {
            board.placeMark(move, currentPlayer);
            currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;
        } catch (IllegalMoveException e) {

        }
    }
}