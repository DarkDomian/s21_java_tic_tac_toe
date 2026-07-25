package dev.trelawnm.tictactoe.domain.service;

import dev.trelawnm.tictactoe.domain.model.Game;

public interface GameService {
    public Game computerMove(Game game);
    public boolean validateGame(Game game);
    public boolean isGameFinished(Game game);
}