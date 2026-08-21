package com.lilamaris.cozyr.reservation.web.request;

import com.lilamaris.cozyr.reservation.application.port.in.command.CreateSeatCommand;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "좌석 생성 요청")
public record CreateSeatRequest(
        @Schema(description = "좌석 식별자", example = "A1")
        @NotBlank String seatId
) {
    public CreateSeatCommand toCommand(long roomId) {
        var id = SeatId.of(roomId, seatId);
        return CreateSeatCommand.of(id);
    }
}
