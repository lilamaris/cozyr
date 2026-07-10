package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateCommentCommand;

public record UpdateCommentRequest(
        String content
) {
    public UpdateCommentCommand toCommand(Long commentId) {
        return UpdateCommentCommand.of(commentId, content);
    }
}
