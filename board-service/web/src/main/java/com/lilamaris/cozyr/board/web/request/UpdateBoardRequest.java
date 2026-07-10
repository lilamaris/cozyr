package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateBoardCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "게시판 수정 요청")
public record UpdateBoardRequest(
        @Schema(description = "게시판 ID", example = "11111111-1111-1111-1111-111111111111")
        UUID boardId,
        @Schema(description = "수정할 게시판 이름", example = "새 공지사항")
        String name,
        @Schema(description = "수정할 게시판 설명", example = "새 공지를 전달하는 게시판입니다.")
        String description
) {
    public UpdateBoardCommand toCommand(UUID boardId) {
        return UpdateBoardCommand.of(boardId, name, description);
    }
}
