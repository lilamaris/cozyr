package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.CreateCommentCommand;

public record CreateCommentRequest(
        String content
) {
    public CreateCommentCommand toCommand(Long postId) {
        return CreateCommentCommand.of(postId, content);
    }
}
