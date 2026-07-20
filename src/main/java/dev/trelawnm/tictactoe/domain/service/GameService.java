package dev.trelawnm.tictactoe.domain.service;

import dev.trelawnm.tictactoe.domain.model.Game;
import dev.trelawnm.tictactoe.domain.model.Move;
import dev.trelawnm.tictactoe.domain.model.Board;

import dev.trelawnm.tictactoe.domain.algorithm.MinimaxEngine;

public class GameService implements GameServiceInterface {

    @Override
    public Game nextMove(Game game) {
        // TODO: return given game with updated board
        Move aiMove = MinimaxEngine.findBestMove(game.getBoard(), game.getCurrentPlayer());
        Board some = game.makeMove(aiMove);
        game.setBoard(some);
        return game;
    }

    @Override
    public boolean validateBoard(Board currBoard) {
        // final GameBoard currBoard = game.getBoard();
        int numOfChanges = 0;
        int sumOnBoard = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int prevCell = this.Board.getCell(i,j);
                final int currCell = currBoard.getCell(i,j);
                if (prevCell != 0 && prevCell != currCell)
                    return false;

                if (prevCell == 0 && Math.abs(currCell) == 1)
                    numOfChanges++;

                sumOnBoard += currCell;
            }
        }

        // check the game sum and number of changes made
        if (Math.abs(sumOnBoard) > 1 || numOfChanges != 1)
            return false;

        return true;
    }

    /**
     * Function to define `terminal state`
     */
    @Override
    public boolean gameEnded(Game game) {
        if (game.getBoard().getWinner() || game.getBoard().isFull())
            return true;
        else
            return false;
    }
}