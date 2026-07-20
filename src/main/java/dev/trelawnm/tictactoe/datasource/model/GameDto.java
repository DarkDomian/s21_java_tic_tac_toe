package dev.trelawnm.tictactoe.datasource.model;

import dev.trelawnm.tictactoe.domain.Player;
import java.util.List;
import java.util.UUID;

public record GameDto(UUID id, List<List<Integer>> field, Player currentPlayer) {
    // TODO: write constructor
}