package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.Board;

import java.time.Instant;
import java.util.UUID;

public record UpdatedBoardResult(
        UUID id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static UpdatedBoardResult from(Board board) {
        return new UpdatedBoardResult(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
