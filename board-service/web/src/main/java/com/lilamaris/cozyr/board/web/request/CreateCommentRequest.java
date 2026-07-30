package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.CreateCommentCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "댓글 생성 요청")
public record CreateCommentRequest(
        @Schema(description = "댓글 본문", example = "좋은 글입니다.")
        @NotBlank String content
) {
    public CreateCommentCommand toCommand(Long postId, UUID authorUserId) {
        return CreateCommentCommand.of(postId, content, authorUserId);
    }
}
