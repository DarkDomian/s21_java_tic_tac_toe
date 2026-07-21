package dev.trelawnm.tictactoe.web.model;

import java.util.List;
import java.util.UUID;

import dev.trelawnm.tictactoe.domain.model.Player;

public record GameWeb(UUID id, List<List<Integer>> field, Player currentPlayer) {
}