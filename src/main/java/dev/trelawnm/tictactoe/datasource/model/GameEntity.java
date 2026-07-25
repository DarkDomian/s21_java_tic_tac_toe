package dev.trelawnm.tictactoe.datasource.model;

import java.util.UUID;

public record GameEntity(UUID uuid, BoardEntity board) {}