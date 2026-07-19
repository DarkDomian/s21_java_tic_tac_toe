package dev.trelawnm.tictactoe.datasource.model;

import java.util.HashMap;
import java.util.UUID;

public class GameDatasource {
    private final HashMap<UUID, GameDto> repo = new HashMap<UUID, GameDto>();

    public GameDto findById(UUID id) {
        return repo.get(id);
    }

    public void save(GameDto toSave) {
        repo.put(toSave);
    }
}