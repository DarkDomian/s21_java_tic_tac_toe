package dev.trelawnm.tictactoe.datasource.repository;

import java.util.UUID;

import dev.trelawnm.tictactoe.datasource.mapper.GameMapper;
import dev.trelawnm.tictactoe.datasource.model.GameDatasource;
import dev.trelawnm.tictactoe.datasource.model.GameDto;
import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.service.GameRepository;

public class InMemoryGameRepository implements GameRepository {
    private final GameDatasource datasource;
    private final GameMapper mapper;

    public InMemoryGameRepository(GameDatasource datasource, GameMapper mapper) {
        this.datasource = datasource;
        this.mapper = mapper;
    }

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