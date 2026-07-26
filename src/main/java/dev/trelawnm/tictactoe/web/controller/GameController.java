package dev.trelawnm.tictactoe.web.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.service.GameService;
import dev.trelawnm.tictactoe.web.mapper.WebMapper;
import dev.trelawnm.tictactoe.web.model.BoardDTO;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game/{uuid}")
    public ResponseEntity<?> move(@PathVariable UUID uuid, @RequestBody BoardDTO board) {
        Game game = WebMapper.toDomain(uuid, board);

        if (game.board().grid().length != 3) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid move"));
        }

        if (gameService.isEndedGame(game)) {
            return ResponseEntity.ok(Map.of("message", "Game is already ended"));
        }

        if (!gameService.isValidGame(game)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid move"));
        }

        Game result = gameService.computerMove(game);

        return ResponseEntity.ok(WebMapper.toDTO(result.board()));
    }
}