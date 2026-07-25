package dev.trelawnm.tictactoe.web.mapper;

import java.util.UUID;

import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.model.Board;

import dev.trelawnm.tictactoe.web.model.GameDTO;
import dev.trelawnm.tictactoe.web.model.BoardDTO;

public class WebMapper {
    public static BoardDTO toDTO(Board board) {
        return new BoardDTO(board.grid());
    }
    public static GameDTO toDTO(Game game) {
        return new GameDTO(game.uuid(), toDTO(game.board()));
    }

    public static Board toDomain(BoardDTO board) {
        return new Board(board.board());
    }
    public static Game toDomain(GameDTO game) {
        return new Game(game.uuid(), toDomain(game.board()));
    }
    public static Game toDomain(UUID uuid, BoardDTO board) {
        return new Game(uuid, toDomain(board));
    }
}