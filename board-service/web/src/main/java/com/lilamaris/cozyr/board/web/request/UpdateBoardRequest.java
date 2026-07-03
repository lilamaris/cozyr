package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateBoardCommand;

import java.util.UUID;

public record UpdateBoardRequest(
        UUID boardId,
        String name,
        String description
) {
    public UpdateBoardCommand toCommand(UUID boardId) {
        return UpdateBoardCommand.of(boardId, name, description);
    }
}
