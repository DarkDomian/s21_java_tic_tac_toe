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

    public Game(UUID id, Board board, Player cp) {
        this.board = board;
        this.uuid = id;
        this.currentPlayer = cp;
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

    public Board makeMove(Move move) {
        try {
            currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;
            return board.placeMark(move, currentPlayer);
        } catch (IllegalMoveException e) {

        }
    }
}