package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.UpdatePostCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "게시글 수정 요청")
public record UpdatePostRequest(
        @Schema(description = "수정할 게시글 카테고리 ID", example = "11111111-1111-1111-1111-111111111111")
        @NotNull UUID categoryId,
        @Schema(description = "수정할 게시글 제목", example = "수정된 게시글 제목")
        @NotBlank String title,
        @Schema(description = "수정할 게시글 본문", example = "수정된 게시글 본문입니다.")
        @NotBlank String content
) {
    public UpdatePostCommand toCommand(long postId, UUID actorUserId) {
        return UpdatePostCommand.of(postId, categoryId, title, content, actorUserId);
    }
}
