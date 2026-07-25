package dev.trelawnm.tictactoe.datasource.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class Storage {
    private final ConcurrentHashMap<UUID, BoardEntity> store;

    public Storage() {
        this.store = new ConcurrentHashMap<UUID, BoardEntity>();
    }

    public BoardEntity findBoard(UUID id) {
        return store.get(id);
    }

    public void saveBoard(UUID id, BoardEntity board) {
        store.put(id, board);
    }
}