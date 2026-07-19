package dev.trelawnm.tictactoe.domain.service;

import dev.trelawnm.tictactoe.domain.model.Board;
import dev.trelawnm.tictactoe.domain.model.Game;

interface GameServiceInterface {
    public Game nextMove(Game game);
    public boolean validateBoard(Board currBoard);
    public boolean gameEnded(Game game);
}