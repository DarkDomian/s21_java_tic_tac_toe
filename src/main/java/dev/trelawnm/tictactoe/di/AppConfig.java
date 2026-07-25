package dev.trelawnm.tictactoe.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.trelawnm.tictactoe.domain.service.Repository;
import dev.trelawnm.tictactoe.datasource.repository.RepositoryImpl;

import dev.trelawnm.tictactoe.domain.service.GameService;
import dev.trelawnm.tictactoe.domain.service.GameServiceImpl;


@Configuration
public class AppConfig {

    @Bean
    public Repository RepositoryImpl() {
        return new RepositoryImpl();
    }

    @Bean
    GameService GameServiceImpl(Repository repo) {
        return new GameServiceImpl(repo);
    }
}