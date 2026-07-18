package dev.trelawnm.tictactoe.domain.service;

import dev.trelawnm.tictactoe.domain.model.CurrentGame;
import dev.trelawnm.tictactoe.domain.model.GameBoard;

public class GameServiceImpl implements GameService {
    private GameBoard prevBoard;

    @Override
    public CurrentGame nextMove() {
        // TODO: return given game with updated board
        return new CurrentGame();
    }

    @Override
    public boolean validateBoard(GameBoard currBoard) {
        // final GameBoard currBoard = game.getBoard();
        int numOfChanges = 0;
        int sumOnBoard = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int prevCell = this.prevBoard.getCell(i,j);
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

        this.prevBoard = currBoard; // TODO: this is side effect... probably shoudn't be there
        return true;
    }

    /**
     * Function to define `terminal state`
     */
    @Override
    public boolean gameEnded(CurrentGame game) {
        if (hasWinner(game) || game.getBoard().isFull())
            return true;
        else
            return false;
    }

    private boolean hasWinner(CurrentGame game) {
        final int[][] board = game.getBoard().getBoard(); // TODO: rename methods

        // check the rows
        if (Math.abs(board[0][0] + board[0][1] + board[0][2]) == 3 )
            return true;
        if (Math.abs(board[1][0] + board[1][1] + board[1][2]) == 3 )
            return true;
        if (Math.abs(board[2][0] + board[2][1] + board[2][2]) == 3 )
            return true;

        // check the columns
        if (Math.abs(board[0][0] + board[1][0] + board[2][0]) == 3 )
            return true;
        if (Math.abs(board[0][1] + board[1][1] + board[2][1]) == 3 )
            return true;
        if (Math.abs(board[0][2] + board[1][2] + board[2][2]) == 3 )
            return true;

        // check the diogonale
        if (Math.abs(board[0][0] + board[1][1] + board[2][2]) == 3 )
            return true;
        if (Math.abs(board[0][2] + board[1][1] + board[0][2]) == 3 )
            return true;

        return false;
    }

    private int evaluate(GameBoard gameBoard) {
        final int[][] board = gameBoard.getBoard();

        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == GameBoard.X)
                    return +10;
                else if (board[row][0] == GameBoard.O)
                    return -10;
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == GameBoard.X)
                    return +10;
                else if (board[0][col] == GameBoard.O)
                    return -10;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == GameBoard.X)
                return +10;
            else if (board[0][0] == GameBoard.O)
                return -10;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == GameBoard.X)
                return +10;
            else if (board[0][2] == GameBoard.O)
                return -10;
        }

        // Else if none of them have won then return 0
        return 0;
    }

    private int minimax() {
        /* 
        
        function minimax(board, depth, isMaximizingPlayer):

            if current board state is a terminal state :
                return value of the board
            
            if isMaximizingPlayer :
                bestVal = -INFINITY 
                for each move in board :
                    value = minimax(board, depth+1, false)
                    bestVal = max( bestVal, value) 
                return bestVal

            else :
                bestVal = +INFINITY 
                for each move in board :
                    value = minimax(board, depth+1, true)
                    bestVal = min( bestVal, value) 
                return bestVal 
        
        */
    }
}