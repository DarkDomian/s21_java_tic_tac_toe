package dev.trelawnm.tictactoe.datasource.repository;

import java.util.UUID;

import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.service.Repository;

import dev.trelawnm.tictactoe.datasource.mapper.DatasourceMapper;
import dev.trelawnm.tictactoe.datasource.model.Storage;
import dev.trelawnm.tictactoe.datasource.model.GameEntity;
import dev.trelawnm.tictactoe.datasource.model.BoardEntity;

public class RepositoryImpl implements Repository {
    private final Storage storage;

    public RepositoryImpl() {
        this.storage = new Storage();
    }
    
    @Override
    public void saveGame(Game game) {
        GameEntity gameForSave = DatasourceMapper.toEntity(game);
        this.storage.saveBoard(gameForSave.uuid(), gameForSave.board());
    }

    @Override
    public Game loadGame(UUID uuid) {
        BoardEntity boardToGive = this.storage.findBoard(uuid);
        if (boardToGive == null) return null;
        
        return DatasourceMapper.toDomain(uuid, boardToGive);
    }
}