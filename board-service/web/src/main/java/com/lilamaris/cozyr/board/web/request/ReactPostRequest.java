package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.ReactPostCommand;
import com.lilamaris.cozyr.board.domain.ReactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "게시글 반응 요청")
public record ReactPostRequest(
        @Schema(description = "게시글 반응 타입", example = "THUMBS_UP")
        ReactionType reactionType
) {
    public ReactPostCommand toCommand(long postId, UUID actorUserId) {
        return ReactPostCommand.of(postId, actorUserId, reactionType);
    }
}
