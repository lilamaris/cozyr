package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.ReplyCommentCommand;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대댓글 생성 요청")
public record ReplyCommentRequest(
        @Schema(description = "대댓글 본문", example = "답글 감사합니다.")
        String content
) {
    public ReplyCommentCommand toCommand(Long parentId) {
        return ReplyCommentCommand.of(parentId, content);
    }
}
