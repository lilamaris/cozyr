package com.lilamaris.cozyr.reservation.web.request;

import com.lilamaris.cozyr.reservation.application.port.in.command.CreateSeatCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "좌석 생성 요청")
public record CreateSeatRequest(
        @Schema(description = "좌석 코드", example = "A1")
        @NotBlank String code
) {
    public CreateSeatCommand toCommand(UUID roomId) {
        return CreateSeatCommand.of(roomId, code);
    }
}
