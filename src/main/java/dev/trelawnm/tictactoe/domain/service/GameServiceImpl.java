package dev.trelawnm.tictactoe.domain.service;

import java.util.List;
import java.util.UUID;

import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.model.Board;
import dev.trelawnm.tictactoe.domain.model.Move;

public class GameServiceImpl implements GameService {
    private final Repository repo;

    public GameServiceImpl(Repository repo) {
        this.repo = repo;
    }

    @Override
    public Game computerMove(Game game) {
        Move best = findBestMove(game);
        if (best == null) return game;

        Board updated = game.placeMark(best, -1);
        Game result = new Game(game.uuid(), updated);
        repo.saveGame(result);
        return result;
    }

    @Override
    public boolean isValidGame(Game currGame) {
        if (currGame.board().grid().length != 3) return false;
        if (currGame.board().grid()[0].length != 3) return false;

        Game prevGame = repo.loadGame(currGame.uuid());
        boolean hasPrevGame = prevGame != null;

        int changes = 0;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int prev = hasPrevGame ? prevGame.board().grid()[r][c] : 0;
                int curr = currGame.board().grid()[r][c];

                if (prev != 0 && prev != curr) return false;
                if (prev == 0 && Math.abs(curr) == 1) changes++;
            }
        }

        return changes == 1;
    }

    @Override
    public boolean isEndedGame(Game game) {
        return game.getWinner() != 0 || game.board().isFull();
    }

    private Move findBestMove(Game game) {
        int bestVal = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> avblMoves = game.getAvailableMoves();

        for (Move move : avblMoves) {
            Board newBoard = game.placeMark(move, -1);
            Game next = new Game(game.uuid(), newBoard);
            int moveVal = minimax(next, 0, false);

            if (moveVal > bestVal) {
                bestMove = move;
                bestVal = moveVal;
            }
        }

        return bestMove;
    }

    private int minimax(Game game, int depth, boolean isMax) {
        int score = evaluate(game);

        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (game.board().isFull()) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (Move move : game.getAvailableMoves()) {
                Board newBoard = game.placeMark(move, -1);
                Game next = new Game(game.uuid(), newBoard);
                best = Math.max(best, minimax(next, depth + 1, false));
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (Move move : game.getAvailableMoves()) {
                Board newBoard = game.placeMark(move, 1);
                Game next = new Game(game.uuid(), newBoard);
                best = Math.min(best, minimax(next, depth + 1, true));
            }
            return best;
        }
    }

    private int evaluate(Game game) {
        int winner = game.getWinner();
        if (winner == -1) return 10;
        if (winner == 1) return -10;
        return 0;
    }
}