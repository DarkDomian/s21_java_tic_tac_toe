package dev.trelawnm.tictactoe.web.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.service.GameService;

import dev.trelawnm.tictactoe.web.model.BoardDTO;
import dev.trelawnm.tictactoe.web.mapper.WebMapper;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game/{uuid}")
    public BoardDTO move(@PathVariable UUID uuid, @RequestBody BoardDTO board) {
        Game game = WebMapper.toDomain(uuid, board);

        if (!gameService.isValidGame(game)) {
            throw new IllegalArgumentException("Invalid board state");
        }

        if (gameService.isEndedGame(game)) {
            return WebMapper.toDTO(game.board());
        }

        Game result = gameService.computerMove(game);

        return WebMapper.toDTO(result.board());
    }
}