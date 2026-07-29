package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.CreateCategoryCommand;

import java.util.UUID;

public record CreateCategoryRequest(
        String name,
        String description
) {
    public CreateCategoryCommand toCommand(UUID boardId, UUID actorUserId) {
        return CreateCategoryCommand.of(boardId, name, description, actorUserId);
    }
}
