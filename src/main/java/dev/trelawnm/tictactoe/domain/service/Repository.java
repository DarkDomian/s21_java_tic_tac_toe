package dev.trelawnm.tictactoe.domain.service;

import java.util.UUID;
import dev.trelawnm.tictactoe.domain.model.Game;

public interface Repository {
    public void saveGame(Game game);
    public Game loadGame(UUID uuid);
}