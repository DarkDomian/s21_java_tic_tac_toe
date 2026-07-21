package dev.trelawnm.tictactoe.domain.service;

import java.util.UUID;

import dev.trelawnm.tictactoe.domain.model.Board;
import dev.trelawnm.tictactoe.domain.model.Game;

interface GameServiceInterface {
    Game nextMove(Game game);
    boolean validateBoard(Board currBoard);
    boolean gameEnded(Game game);

    Game getGame(UUID id);
    void saveGame(Game game);
}