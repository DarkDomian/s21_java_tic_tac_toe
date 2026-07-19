package dev.trelawnm.tictactoe.domain.service;

import dev.trelawnm.tictactoe.domain.model.Game;
import java.util.UUID;

public interface GameRepository {
    Game findById(UUID gameId);
    void save(Game game);
}