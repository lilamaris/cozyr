package com.lilamaris.cozyr.reservation.web.controller;

import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.cozyr.reservation.application.port.in.ReserveSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.result.ReserveSeatResult;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import com.lilamaris.cozyr.reservation.web.request.ReserveSeatRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "예약 API")
public class ReservationController {
    private final ReserveSeatUseCase reserveSeatUseCase;
    private final IdentityContextHolder identityContextHolder;

    @Operation(summary = "좌석 예약", description = "좌석을 예약합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좌석 예약 성공",
                    content = @Content(schema = @Schema(implementation = ReserveSeatResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "좌석을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "해당 시간의 좌석은 이미 예약됨",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/rooms/{roomId}/seats/{seatId}")
    public ResponseEntity<ReserveSeatResult> reserveSeat(
            @Parameter(
                    description = "방 ID",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable("roomId") long roomId,
            @Parameter(
                    description = "좌석 식별자",
                    required = true,
                    schema = @Schema(type = "string", example = "A1")
            )
            @PathVariable("seatId") String seatId,
            @Valid @RequestBody ReserveSeatRequest body
    ) {
        var identity = identityContextHolder.get();
        var command = body.toCommand(identity.id(), SeatId.of(roomId, seatId));
        var result = reserveSeatUseCase.reserve(command);

        return ResponseEntity.ok(result);
    }
}
