package dev.trelawnm.tictactoe.domain.model;

public enum Player {
    X(1),
    O(-1);

    private final int symbol;

    Player(int symbol) {
        this.symbol = symbol;
    }

    public int getSymbol() {
        return symbol;
    }

    public Player opponent() {
        return this == X ? O : X;
    }

    public static Player fromSymbol(int symbol) {
        if (symbol == X.symbol) return X;
        if (symbol == O.symbol) return O;
        throw new IllegalArgumentException("Invalid symbol: " + symbol);
    }
}