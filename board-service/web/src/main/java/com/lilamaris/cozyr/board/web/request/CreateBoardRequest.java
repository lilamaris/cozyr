package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.CreateBoardCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "게시판 생성 요청")
public record CreateBoardRequest(
        @Schema(description = "게시판 이름", example = "공지사항")
        @NotBlank String name,
        @Schema(description = "게시판 설명", example = "서비스 공지를 전달하는 게시판입니다.")
        @NotBlank String description
) {
    public CreateBoardCommand toCommand() {
        return CreateBoardCommand.of(name, description);
    }
}
