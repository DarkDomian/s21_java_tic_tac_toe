package dev.trelawnm.tictactoe.domain.service;

import dev.trelawnm.tictactoe.domain.model.GameBoard;
import dev.trelawnm.tictactoe.domain.model.CurrentGame;

interface GameService {
    public CurrentGame nextMove();
    public boolean validateBoard(GameBoard currBoard);
    public boolean gameEnded(CurrentGame game);
}