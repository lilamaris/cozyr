package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.ReplyCommentCommand;

public record ReplyCommentRequest(
        String content
) {
    public ReplyCommentCommand toCommand(Long parentId) {
        return ReplyCommentCommand.of(parentId, content);
    }
}
