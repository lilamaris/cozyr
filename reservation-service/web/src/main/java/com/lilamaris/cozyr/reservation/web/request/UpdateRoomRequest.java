package com.lilamaris.cozyr.reservation.web.request;

import com.lilamaris.cozyr.reservation.application.port.in.command.UpdateRoomCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "방 수정 요청")
public record UpdateRoomRequest(
        @Schema(description = "방 이름", example = "VIP 방")
        @NotBlank String name,
        @Schema(description = "방 설명", example = "최상위 좌석을 갖춘 VIP 전용 방입니다.")
        @NotBlank String description
) {
    public UpdateRoomCommand toCommand(long roomId) {
        return UpdateRoomCommand.of(roomId, name, description);
    }
}
