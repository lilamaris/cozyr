package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.CreateCategoryCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "카테고리 생성 요청")
public record CreateCategoryRequest(
        @Schema(description = "카테고리 이름", example = "일반")
        String name,
        @Schema(description = "카테고리 설명", example = "일반 주제를 다루는 카테고리입니다.")
        String description
) {
    public CreateCategoryCommand toCommand(UUID boardId, UUID actorUserId) {
        return CreateCategoryCommand.of(boardId, name, description, actorUserId);
    }
}
