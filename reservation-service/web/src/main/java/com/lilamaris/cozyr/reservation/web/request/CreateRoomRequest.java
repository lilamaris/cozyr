package com.lilamaris.cozyr.reservation.web.request;

import com.lilamaris.cozyr.reservation.application.port.in.command.CreateRoomCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "방 생성 요청")
public record CreateRoomRequest(
        @Schema(description = "방 이름", example = "VIP 방")
        @NotBlank String name,
        @Schema(description = "방 설명", example = "최상위 좌석을 갖춘 VIP 전용 방입니다.")
        @NotBlank String description
) {
    public CreateRoomCommand toCommand() {
        return CreateRoomCommand.of(name, description);
    }
}
