package dev.trelawnm.tictactoe.domain.service;

import dev.trelawnm.tictactoe.domain.model.Game;

public class GameServiceImpl implements GameService {
    private final Repository repo; // has methods .saveGame(Game game) & .loadGame(UUID uuid)

    public GameServiceImpl(Repository repo) {
        this.repo = repo;
    }

    @Override
    public Game computerMove(Game game){
        return null;
    }

    @Override
    public boolean validateGame(Game game){
        return false;
    }

    @Override
    public boolean isGameFinished(Game game){
        return false;
    }
}