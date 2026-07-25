package dev.trelawnm.tictactoe.datasource.repository;

import java.util.UUID;
import dev.trelawnm.tictactoe.domain.model.Game;

import dev.trelawnm.tictactoe.domain.service.Repository;
import dev.trelawnm.tictactoe.datasource.model.Storage;

public class RepositoryImpl implements Repository {
    private final Storage storage;

    public RepositoryImpl() {
        this.storage = new Storage();
    }
    
    @Override
    public void saveGame(Game game) {

    }

    @Override
    public Game loadGame(UUID uuid) {
        return null;
    }
}