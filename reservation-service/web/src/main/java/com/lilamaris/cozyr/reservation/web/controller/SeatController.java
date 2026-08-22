package com.lilamaris.cozyr.reservation.web.controller;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.application.port.in.CreateSeatUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.FindSeatDetailUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindSeatDetailQuery;
import com.lilamaris.cozyr.reservation.application.port.in.result.SeatCreatedResult;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import com.lilamaris.cozyr.reservation.web.request.CreateSeatRequest;
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

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/seats")
@RequiredArgsConstructor
@Tag(name = "Seats", description = "좌석 API")
public class SeatController {
    private final CreateSeatUseCase createSeatUseCase;
    private final FindSeatDetailUseCase findSeatDetailUseCase;

    @Operation(summary = "좌석 생성", description = "방에 새 좌석을 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "좌석 생성 성공",
                    content = @Content(schema = @Schema(implementation = SeatCreatedResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "방을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "동일한 좌석이 이미 존재",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<SeatCreatedResult> createSeat(
            @Parameter(
                    description = "방 ID",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable("roomId") long roomId,
            @Valid @RequestBody CreateSeatRequest body
    ) {
        var command = body.toCommand(roomId);
        var result = createSeatUseCase.create(command);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.seatId())
                .toUri();

        return ResponseEntity.created(location).body(result);
    }

    @Operation(summary = "좌석 상세 조회", description = "좌석 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좌석 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = SeatDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "좌석을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{seatId}")
    public ResponseEntity<SeatDetail> findSeatDetail(
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
            @PathVariable("seatId") String seatId
    ) {
        var query = FindSeatDetailQuery.of(SeatId.of(roomId, seatId));
        var result = findSeatDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }
}
