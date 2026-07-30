package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.ReplyCommentCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "대댓글 생성 요청")
public record ReplyCommentRequest(
        @Schema(description = "대댓글 본문", example = "답글 감사합니다.")
        @NotBlank String content
) {
    public ReplyCommentCommand toCommand(Long parentId, UUID authorUserId) {
        return ReplyCommentCommand.of(parentId, content, authorUserId);
    }
}
