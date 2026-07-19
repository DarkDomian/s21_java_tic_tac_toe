package dev.trelawnm.tictactoe.domain.exceptions;

public class IllegalMoveException extends Exception {
    public IllegalMoveException(String message) {
        super(message);
    }
}