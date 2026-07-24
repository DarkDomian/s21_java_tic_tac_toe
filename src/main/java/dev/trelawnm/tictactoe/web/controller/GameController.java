package dev.trelawnm.tictactoe.web.controller;

import dev.trelawnm.tictactoe.domain.model.Board;
import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.model.Player;
import dev.trelawnm.tictactoe.domain.service.GameService;
import dev.trelawnm.tictactoe.web.mapper.GameWebMapper;
import dev.trelawnm.tictactoe.web.model.GameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class GameController {

    private final GameService gameService;
    private final GameWebMapper webMapper;

    @Autowired
    public GameController(GameService gameService, GameWebMapper webMapper) {
        this.gameService = gameService;
        this.webMapper = webMapper;
    }

    @PostMapping("/game/{uuid}")
    public ResponseEntity<?> makeMove(@PathVariable UUID uuid, @RequestBody GameDto clientBoard) {

        Game game = new Game(uuid, webMapper.toBoard(clientBoard.field()));

        // Validate (generate new game, if not exist)
        if (gameService.validateBoard(game)) {
            return ResponseEntity.badRequest().body("Bad move");
        }

        // Check does the game is ended
        if (gameService.gameEnded(game)) {
            return ResponseEntity.badRequest().body("Game is already ended");
        }

        return webMapper.boardToList(gameService.nextMove(game).getBoard());
    }
}

const gen_uuid = crypto.randomUUID();
const board = [[1,0,0],[0,0,0],[0,0,0]];

fetch(`http://localhost:8080/game/${gen_uuid}`, {
      method: 'POST',
      headers: {'Content-Typs': 'application/json'},
			body: JSON.stringify({board: {board}})
      })
      .then(r=>r.json())
			.then(console.log)