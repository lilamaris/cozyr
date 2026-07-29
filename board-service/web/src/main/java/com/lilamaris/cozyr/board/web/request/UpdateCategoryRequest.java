package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateCategoryCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "카테고리 수정 요청")
public record UpdateCategoryRequest(
        @Schema(description = "수정할 카테고리 이름", example = "새 일반")
        String name,
        @Schema(description = "수정할 카테고리 설명", example = "새 일반 주제를 다루는 카테고리입니다.")
        String description
) {
    public UpdateCategoryCommand toCommand(UUID categoryId, UUID actorUserId) {
        return UpdateCategoryCommand.of(categoryId, name, description, actorUserId);
    }
}
