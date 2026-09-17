package com.lilamaris.cozyr.reservation.web.request;

import com.lilamaris.cozyr.reservation.application.port.in.command.ReserveSeatCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Schema(description = "좌석 예약 요청")
public record ReserveSeatRequest(
        @Schema(description = "예약 날짜", example = "2026-01-15")
        @NotNull LocalDate reserveDate,
        @Schema(description = "예약할 스케줄 슬롯 ID 목록")
        @NotEmpty Set<@NotNull UUID> scheduleSlotIds
) {
    public ReserveSeatCommand toCommand(UUID reserveUserId, UUID roomId, UUID seatId) {
        return ReserveSeatCommand.of(reserveUserId, roomId, seatId, reserveDate, scheduleSlotIds);
    }
}
