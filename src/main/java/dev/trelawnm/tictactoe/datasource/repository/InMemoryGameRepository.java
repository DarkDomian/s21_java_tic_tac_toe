package dev.trelawnm.tictactoe.datasource.repository;

import dev.trelawnm.tictactoe.datasource.model.GameDatasource;
import dev.trelawnm.tictactoe.datasource.mapper.GameMapper;

public class InMemoryGameRepository implements GameRepository {
    private final GameDatasource datasource;
    private final GameMapper mapper;

    @Override
    public Game findById(UUID gameId) {
        // 1. Получить DTO из datasource
        GameDto dto = datasource.findById(gameId);
        // 2. Спреобразовать DTO в доменный объект
        return mapper.toDomain(dto);
    }

    @Override
    public void save(Game game) {
        // 1. Преобразовать доменный объект в DTO
        GameDto dto = mapper.toDto(game);
        // 2. Сохранить DTO в datasource
        datasource.save(dto);
    }
}