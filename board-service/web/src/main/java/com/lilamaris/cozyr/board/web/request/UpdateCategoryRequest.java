package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateCategoryCommand;

import java.util.UUID;

public record UpdateCategoryRequest(
        String name,
        String description
) {
    public UpdateCategoryCommand toCommand(UUID categoryId, UUID actorUserId) {
        return UpdateCategoryCommand.of(categoryId, name, description, actorUserId);
    }
}
