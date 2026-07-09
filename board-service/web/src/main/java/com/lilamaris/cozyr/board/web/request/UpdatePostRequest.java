package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.UpdatePostCommand;

public record UpdatePostRequest(
        String title,
        String content
) {
    public UpdatePostCommand toCommand(long postId) {
        return UpdatePostCommand.of(postId, title, content);
    }
}
