package dev.trelawnm.tictactoe.datasource.mapper;

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

    private List<List<Integer>> boardToList(int[][] grid) { ... }
    private int[][] listToBoard(List<List<Integer>> list) { ... }
}