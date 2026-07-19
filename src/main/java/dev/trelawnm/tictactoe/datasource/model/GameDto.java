package dev.trelawnm.tictactoe.datasource.model;

import dev.trelawnm.tictactoe.domain.Player; // если общий enum
import java.util.List;
import java.util.UUID;

public record GameDto(UUID id, List<List<Integer>> field, Player currentPlayer) {
    // Можно также добавить конструктор для удобства
}