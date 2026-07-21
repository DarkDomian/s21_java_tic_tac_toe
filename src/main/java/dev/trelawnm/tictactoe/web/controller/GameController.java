package dev.trelawnm.tictactoe.web.controller;

import dev.trelawnm.tictactoe.domain.model.Board;
import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.model.Player;
import dev.trelawnm.tictactoe.domain.service.GameService;
import dev.trelawnm.tictactoe.web.mapper.GameWebMapper;
import dev.trelawnm.tictactoe.web.model.GameWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final GameWebMapper webMapper;

    @Autowired
    public GameController(GameService gameService, GameWebMapper webMapper) {
        this.gameService = gameService;
        this.webMapper = webMapper;
    }

    @PostMapping("/{uuid}")
    public ResponseEntity<?> makeMove(@PathVariable UUID uuid, @RequestBody GameWeb clientGame) {
        // 1. Проверка соответствия UUID
        if (!uuid.equals(clientGame.id())) {
            return ResponseEntity.badRequest().body("UUID in path does not match UUID in request body");
        }

        // 2. Получение текущей игры из хранилища
        Game currentGame = gameService.getGame(uuid);
        if (currentGame == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        // 3. Проверка, не завершена ли игра
        if (gameService.gameEnded(currentGame)) {
            return ResponseEntity.badRequest().body("Game is already ended");
        }

        // 4. Валидация хода пользователя
        int[][] currentGrid = currentGame.getBoard().grid();
        List<List<Integer>> clientField = clientGame.field();

        if (clientField.size() != 3 || clientField.get(0).size() != 3) {
            return ResponseEntity.badRequest().body("Invalid board size");
        }

        int changes = 0;
        int changeRow = -1, changeCol = -1;
        int newValue = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int currentVal = currentGrid[i][j];
                int clientVal = clientField.get(i).get(j);
                if (currentVal != clientVal) {
                    changes++;
                    changeRow = i;
                    changeCol = j;
                    newValue = clientVal;
                }
            }
        }

        if (changes != 1) {
            return ResponseEntity.badRequest().body("Exactly one cell must be changed");
        }
        if (currentGrid[changeRow][changeCol] != 0) {
            return ResponseEntity.badRequest().body("Cell was not empty");
        }
        Player currentPlayer = currentGame.getCurrentPlayer();
        if (Player.fromSymbol(newValue) != currentPlayer) {
            return ResponseEntity.badRequest().body("Invalid symbol for current player");
        }

        // 5. Применение хода пользователя
        int[][] newGrid = deepCopy(currentGrid);
        newGrid[changeRow][changeCol] = newValue;
        Board updatedBoard = new Board(newGrid);
        Game updatedGame = new Game(currentGame.getId(), updatedBoard, currentPlayer.opponent());

        // 6. Если игра не завершена, компьютер делает ход
        Game resultGame;
        if (gameService.gameEnded(updatedGame)) {
            resultGame = updatedGame;
        } else {
            resultGame = gameService.nextMove(updatedGame);
        }

        // 7. Сохранение и возврат ответа
        gameService.saveGame(resultGame);
        GameWeb response = webMapper.toWeb(resultGame);
        return ResponseEntity.ok(response);
    }

    private int[][] deepCopy(int[][] src) {
        int[][] copy = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, copy[i], 0, src[i].length);
        }
        return copy;
    }
}