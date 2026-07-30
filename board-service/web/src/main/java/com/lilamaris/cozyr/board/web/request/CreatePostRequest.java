package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.CreatePostCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "게시글 생성 요청")
public record CreatePostRequest(
        @Schema(description = "게시글이 속할 카테고리 ID", example = "11111111-1111-1111-1111-111111111111")
        @NotNull UUID categoryId,
        @Schema(description = "게시글 제목", example = "첫 번째 게시글")
        @NotBlank String title,
        @Schema(description = "게시글 본문", example = "게시글 본문입니다.")
        @NotBlank String content
) {
    public CreatePostCommand toCommand(UUID boardId, UUID authorUserId) {
        return CreatePostCommand.of(boardId, categoryId, title, content, authorUserId);
    }
}
