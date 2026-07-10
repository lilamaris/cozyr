package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateCommentCommand;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 수정 요청")
public record UpdateCommentRequest(
        @Schema(description = "수정할 댓글 본문", example = "수정된 댓글입니다.")
        String content
) {
    public UpdateCommentCommand toCommand(Long commentId) {
        return UpdateCommentCommand.of(commentId, content);
    }
}
