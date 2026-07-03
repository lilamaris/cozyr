package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.Board;

import java.time.Instant;
import java.util.UUID;

public record CreatedBoardResult(
        UUID id,
        String name,
        String description,
        Instant createdAt
) {
    public static CreatedBoardResult from(Board board) {
        return new CreatedBoardResult(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.getCreatedAt()
        );
    }
}
