package dev.trelawnm.tictactoe.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.trelawnm.tictactoe.datasource.mapper.GameMapper;
import dev.trelawnm.tictactoe.datasource.model.GameDatasource;
import dev.trelawnm.tictactoe.datasource.repository.InMemoryGameRepository;
import dev.trelawnm.tictactoe.domain.service.GameRepository;
import dev.trelawnm.tictactoe.domain.service.GameService;

@Configuration
public class AppConfig {

    @Bean
    public GameDatasource gameDatasource() {
        return new GameDatasource();
    }

    @Bean
    public GameMapper gameMapper() {
        return new GameMapper();
    }

    @Bean
    public GameRepository gameRepository(GameDatasource datasource, GameMapper mapper) {
        return new InMemoryGameRepository(datasource, mapper);
    }

    @Bean
    public GameService gameService(GameRepository repository) {
        return new GameService(repository);
    }
}