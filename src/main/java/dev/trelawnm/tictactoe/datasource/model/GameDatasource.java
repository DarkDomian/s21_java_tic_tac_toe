package dev.trelawnm.tictactoe.datasource.model;

import java.util.ConcurrentHashMap;
import java.util.UUID;

public class GameDatasource {
    private final ConcurrentHashMap<UUID, GameDto> repo = new ConcurrentHashMap<UUID, GameDto>();

    public GameDto findById(UUID id) {
        return repo.get(id);
    }

    public void save(GameDto toSave) {
        repo.put(toSave.id, toSave);
    }
}