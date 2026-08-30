package com.lilamaris.cozyr.reservation.web.controller;

import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationDetail;
import com.lilamaris.cozyr.reservation.application.model.seat.ReservableSeatSchedule;
import com.lilamaris.cozyr.reservation.application.port.in.CancelReserveUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.FindReservableSeatScheduleUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.FindReservationDetailUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.ReserveSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CancelReserveCommand;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindReservableSeatScheduleQuery;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindReservationDetailQuery;
import com.lilamaris.cozyr.reservation.application.port.in.result.CancelReserveResult;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "예약 API")
public class ReservationController {
    private final ReserveSeatUseCase reserveSeatUseCase;
    private final CancelReserveUseCase cancelReserveUseCase;
    private final FindReservableSeatScheduleUseCase findReservableSeatScheduleUseCase;
    private final FindReservationDetailUseCase findReservationDetailUseCase;

    private final IdentityContextHolder identityContextHolder;
    private final Clock clock;

    @Operation(summary = "좌석 예약 가능 시간 조회", description = "좌석의 예약 가능 시간 구역을 조회합니다. targetDate를 제공하지 않으면 현재 날짜 기준 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좌석 예약 가능 시간 조회 성공",
                    content = @Content(schema = @Schema(implementation = ReservableSeatSchedule.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/rooms/{roomId}/seats/{seatId}")
    public ResponseEntity<ReservableSeatSchedule> findReservableSeatSchedule(
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
            @Parameter(
                    description = "조회할 날짜. 미지정 시 현재 날짜",
                    schema = @Schema(type = "string", format = "date", example = "2026-01-15")
            )
            @RequestParam(name = "targetDate", required = false) LocalDate targetDate
    ) {
        var targetDateOrDefault = targetDate != null ? targetDate : LocalDate.now(clock);
        var query = FindReservableSeatScheduleQuery.of(targetDateOrDefault, SeatId.of(roomId, seatId));
        var result = findReservableSeatScheduleUseCase.find(query);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "예약 상세 조회", description = "예약 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "예약 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = ReservationDetail.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "예약을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDetail> findReservationDetail(
            @Parameter(
                    description = "예약 ID",
                    required = true,
                    schema = @Schema(type = "string", example = "9f1c8a4e-9d61-4e10-a629-bf68068073b7")
            )
            @PathVariable("reservationId") UUID reservationId
    ) {
        var query = FindReservationDetailQuery.of(reservationId);
        var result = findReservationDetailUseCase.find(query);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "예약 취소", description = "예약을 취소합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "예약 취소 성공",
                    content = @Content(schema = @Schema(implementation = CancelReserveResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "예약을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<CancelReserveResult> cancelReservation(
            @Parameter(
                    description = "예약 ID",
                    required = true,
                    schema = @Schema(type = "string", example = "9f1c8a4e-9d61-4e10-a629-bf68068073b7")
            )
            @PathVariable("reservationId") UUID reservationId
    ) {
        var command = CancelReserveCommand.of(reservationId);
        var result = cancelReserveUseCase.cancel(command);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "좌석 예약", description = "좌석을 예약합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "좌석 예약 생성 성공",
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

        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/reservation/{reservationId}")
                .buildAndExpand(result.reservationId())
                .toUri();

        return ResponseEntity.created(location).body(result);
    }
}
