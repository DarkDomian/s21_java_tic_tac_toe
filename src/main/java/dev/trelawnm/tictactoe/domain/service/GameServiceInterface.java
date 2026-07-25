package dev.trelawnm.tictactoe.domain.service;

import dev.trelawnm.tictactoe.domain.model.Game;

interface GameServiceInterface {
    Game nextMove(Game game);
    boolean validateBoard(Game game);
    boolean gameEnded(Game game);
}