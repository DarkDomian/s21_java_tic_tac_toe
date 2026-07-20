package dev.trelawnm.tictactoe.domain.algorithm;

import java.util.List;

import dev.trelawnm.tictactoe.domain.exceptions.IllegalMoveException;
import dev.trelawnm.tictactoe.domain.model.Board;
import dev.trelawnm.tictactoe.domain.model.Move;
import dev.trelawnm.tictactoe.domain.model.Player;

public class MinimaxEngine {

    public static Move findBestMove(Board board, Player currPlayer) throws IllegalMoveException {
        int bestVal = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> avblMoves = board.getAvailableMoves();
        int depth = board.grid().length * board.grid()[0].length - avblMoves.size();

        for (Move move : avblMoves) {
            Board newBoard = board.placeMark(move, currPlayer);

            int moveVal = minimax(newBoard, depth, currPlayer == Player.X);

            if (moveVal > bestVal) {
                bestMove = new Move(move.row(), move.col());
                bestVal = moveVal;
            }
        }

        return bestMove;
    }

    private static int minimax(Board board, int depth, Boolean isMax) throws IllegalMoveException {
        int score = evaluate(board);

        // If Maximizer has won the game 
        // return his/her evaluated score
        if (score == 10)
            return score - depth;

        // If Minimizer has won the game 
        // return his/her evaluated score
        if (score == -10)
            return score + depth;

        // If there are no more moves and 
        // no winner then it is a tie
        if (board.isFull())
            return 0;

        // maximizer's move
        if (isMax) {
            int best = Integer.MIN_VALUE;

            for (Move move : board.getAvailableMoves()) {
                Board newBoard = board.placeMark(move, Player.X);

                best = Math.max(best, minimax(newBoard, depth + 1, !isMax));
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            for (Move move : board.getAvailableMoves()) {
                Board newBoard = board.placeMark(move, Player.O);

                best = Math.min(best, minimax(newBoard, depth + 1, !isMax));
            }
            return best;
        }
    }


    private static int evaluate(Board board) {
        Player winner = board.getWinner();
        if (winner != null) return winner == Player.X ? +10 : -10;

        return 0;
    }

}