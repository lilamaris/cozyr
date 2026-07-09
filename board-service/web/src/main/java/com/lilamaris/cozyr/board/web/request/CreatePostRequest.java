package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.CreatePostCommand;

import java.util.UUID;

public record CreatePostRequest(
        String title,
        String content
) {
    public CreatePostCommand toCommand(UUID boardId) {
        return CreatePostCommand.of(boardId, title, content);
    }
}
