package dev.trelawnm.tictactoe.datasource.mapper;

import java.util.List;

public class GameMapper {
    public GameDto toDto(Game game) {
        return new GameDto(
            game.getId(),
            boardToList(game.getBoard().grid()), // int[][] -> List<List<Integer>>
            game.getCurrentPlayer()
        );
    }

    public Game toDomain(GameDto dto) {
        return new Game(
            dto.id(),
            new Board(listToBoard(dto.field())),
            dto.currentPlayer()
        );
    }

    private List<List<Integer>> boardToList(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return new ArrayList<>(); // или вернуть null, смотря по логике
        }
        int rows = grid.length;
        int cols = grid[0].length; // предполагаем, что все строки одинаковой длины
        List<List<Integer>> transformed = new ArrayList<>(rows);
        for (int[] row : grid) {
            List<Integer> rowList = new ArrayList<>(cols);
            for (int value : row) {
                rowList.add(value);
            }
            transformed.add(rowList);
        }
        return transformed;
    }

    private int[][] listToBoard(List<List<Integer>> list) {
        if (list == null || list.isEmpty()) {
            return new int[0][0];
        }
        int rows = list.size();
        int cols = list.get(0).size(); // предполагаем, что все внутренние списки одинаковой длины
        int[][] grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            List<Integer> rowList = list.get(i);
            // На случай, если строки разной длины – берем минимум, чтобы избежать исключения
            int currentCols = Math.min(cols, rowList.size());
            for (int j = 0; j < currentCols; j++) {
                grid[i][j] = rowList.get(j);
            }
            // Если в rowList меньше элементов, остальные останутся 0 (значение по умолчанию)
        }
        return grid;
    }
}