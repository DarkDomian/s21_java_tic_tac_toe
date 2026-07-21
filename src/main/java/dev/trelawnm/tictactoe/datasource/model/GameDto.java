package dev.trelawnm.tictactoe.datasource.model;

import java.util.List;
import java.util.UUID;

import dev.trelawnm.tictactoe.domain.model.Player;

public record GameDto(UUID id, List<List<Integer>> field, Player currentPlayer) {
    // TODO: write constructor
}