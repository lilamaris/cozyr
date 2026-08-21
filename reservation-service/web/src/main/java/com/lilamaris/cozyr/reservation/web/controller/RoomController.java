package com.lilamaris.cozyr.reservation.web.controller;

import com.lilamaris.cozyr.reservation.application.model.room.RoomCursor;
import com.lilamaris.cozyr.reservation.application.model.room.RoomDetail;
import com.lilamaris.cozyr.reservation.application.model.room.RoomFilter;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;
import com.lilamaris.cozyr.reservation.application.port.in.CreateRoomUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.FindRoomDetailUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.ListRoomSummaryUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.UpdateRoomUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindRoomDetailQuery;
import com.lilamaris.cozyr.reservation.application.port.in.query.ListRoomSummaryQuery;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomCreatedResult;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomUpdatedResult;
import com.lilamaris.cozyr.reservation.web.request.CreateRoomRequest;
import com.lilamaris.cozyr.reservation.web.request.UpdateRoomRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
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

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "방 API")
public class RoomController {
    private final CreateRoomUseCase createRoomUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;

    private final ListRoomSummaryUseCase listRoomSummaryUseCase;
    private final FindRoomDetailUseCase findRoomDetailUseCase;

    @Operation(summary = "방 목록 조회", description = "방을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "방 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping
    public ResponseEntity<CursorResult<RoomSummary, RoomCursor>> listRoom(
            @Parameter(description = "방 이름 검색어", schema = @Schema(type = "string", example = "VIP"))
            @RequestParam(name = "name", required = false) String name,
            @Parameter(description = "방 설명 검색어", schema = @Schema(type = "string", example = "VIP 전용"))
            @RequestParam(name = "description", required = false) String description,
            @Parameter(description = "커서 방 ID", schema = @Schema(type = "integer", format = "int64"))
            @RequestParam(name = "rid", required = false) Long roomId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, schema = @Schema(type = "integer", minimum = "1", example = "20"))
            @RequestParam(name = "size") int size
    ) {
        RoomCursor cursor = null;
        if (createdAt != null && roomId != null) {
            cursor = RoomCursor.of(createdAt, roomId);
        }

        var filter = RoomFilter.empty()
                .withName(name)
                .withDescription(description);

        var query = ListRoomSummaryQuery.of(filter, cursor, size);
        var result = listRoomSummaryUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "방 상세 조회", description = "방 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "방 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = RoomDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "방을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDetail> findRoomDetail(
            @Parameter(
                    description = "방 ID",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable("roomId") long roomId
    ) {
        var query = FindRoomDetailQuery.of(roomId);
        var result = findRoomDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "방 생성", description = "새 방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "방 생성 성공",
                    content = @Content(schema = @Schema(implementation = RoomCreatedResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<RoomCreatedResult> createRoom(
            @Valid @RequestBody CreateRoomRequest body
    ) {
        var command = body.toCommand();
        var result = createRoomUseCase.create(command);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.roomId())
                .toUri();

        return ResponseEntity.created(location).body(result);
    }

    @Operation(summary = "방 수정", description = "방 이름과 설명을 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "방 수정 성공",
                    content = @Content(schema = @Schema(implementation = RoomUpdatedResult.class))
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
            )
    })
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomUpdatedResult> updateRoom(
            @Parameter(
                    description = "방 ID",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable("roomId") long roomId,
            @Valid @RequestBody UpdateRoomRequest body
    ) {
        var command = body.toCommand(roomId);
        var result = updateRoomUseCase.update(command);

        return ResponseEntity.ok(result);
    }
}
