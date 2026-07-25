package dev.trelawnm.tictactoe.datasource.mapper;

import java.util.UUID;

import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.model.Board;

import dev.trelawnm.tictactoe.datasource.model.BoardEntity;
import dev.trelawnm.tictactoe.datasource.model.GameEntity;

public class DatasourceMapper {
    public static BoardEntity toEntity(Board board) {
        return new BoardEntity(board.grid());
    }
    public static GameEntity toEntity(Game game) {
        return new GameEntity(game.uuid(), toEntity(game.board()));
    }

    public static Board toDomain(BoardEntity board) {
        return new Board(board.board());
    }
    public static Game toDomain(GameEntity game) {
        return new Game(game.uuid(), toDomain(game.board()));
    }
    public static Game toDomain(UUID uuid, BoardEntity board) {
        return new Game(uuid, toDomain(board));
    }
}