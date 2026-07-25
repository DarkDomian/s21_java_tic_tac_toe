package dev.trelawnm.tictactoe.web.model;

import java.util.UUID;

public record GameDTO(UUID uuid, BoardDTO board) {}