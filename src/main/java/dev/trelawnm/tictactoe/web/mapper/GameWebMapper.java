package dev.trelawnm.tictactoe.web.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import dev.trelawnm.tictactoe.domain.model.Board;
import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.web.model.GameWeb;

@Component
public class GameWebMapper {

    public GameWeb toWeb(Game game) {
        return new GameWeb(
            game.getId(),
            boardToList(game.getBoard().grid()),
            game.getCurrentPlayer()
        );
    }

    public Board toBoard(List<List<Integer>> field) {
        int rows = field.size();
        int cols = field.get(0).size();
        int[][] grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = field.get(i).get(j);
            }
        }
        return new Board(grid);
    }

    private List<List<Integer>> boardToList(int[][] grid) {
        List<List<Integer>> list = new ArrayList<>();
        for (int[] row : grid) {
            List<Integer> rowList = new ArrayList<>();
            for (int val : row) {
                rowList.add(val);
            }
            list.add(rowList);
        }
        return list;
    }
}