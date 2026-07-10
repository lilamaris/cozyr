package com.lilamaris.cozyr.board.web.controller;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.model.board.BoardFilter;
import com.lilamaris.cozyr.board.application.model.board.BoardSummary;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.CreateBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.FindBoardDetailUseCase;
import com.lilamaris.cozyr.board.application.port.in.ListBoardSummaryUseCase;
import com.lilamaris.cozyr.board.application.port.in.UpdateBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.FindBoardDetailQuery;
import com.lilamaris.cozyr.board.application.port.in.query.ListBoardSummaryQuery;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedBoardResult;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedBoardResult;
import com.lilamaris.cozyr.board.web.request.CreateBoardRequest;
import com.lilamaris.cozyr.board.web.request.UpdateBoardRequest;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Boards", description = "게시판 API")
public class BoardController {
    private final CreateBoardUseCase createBoardUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;

    private final FindBoardDetailUseCase findBoardDetailUseCase;
    private final ListBoardSummaryUseCase listBoardSummaryUseCase;

    @Operation(summary = "게시판 생성", description = "새 게시판을 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "게시판 생성 성공",
                    content = @Content(schema = @Schema(implementation = CreatedBoardResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<CreatedBoardResult> create(
            @Valid @RequestBody CreateBoardRequest body
    ) {
        var command = body.toCommand();
        var result = createBoardUseCase.create(command);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{boardId}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @Operation(summary = "게시판 수정", description = "게시판 이름과 설명을 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시판 수정 성공",
                    content = @Content(schema = @Schema(implementation = UpdatedBoardResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시판을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/{boardId}")
    public ResponseEntity<UpdatedBoardResult> update(
            @Parameter(
                    description = "게시판 ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable("boardId") UUID boardId,
            @Valid @RequestBody UpdateBoardRequest body
    ) {
        var command = body.toCommand(boardId);
        var result = updateBoardUseCase.update(command);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "게시판 목록 조회", description = "게시판을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시판 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping
    public ResponseEntity<CursorResult<BoardSummary, BoardCursor>> list(
            @Parameter(description = "게시판 이름 검색어", schema = @Schema(type = "string", example = "공지"))
            @RequestParam(name = "name", required = false) String name,
            @Parameter(description = "게시판 설명 검색어", schema = @Schema(type = "string", example = "알림"))
            @RequestParam(name = "description", required = false) String description,
            @Parameter(description = "커서 게시판 ID", schema = @Schema(type = "string", format = "uuid"))
            @RequestParam(name = "bid", required = false) UUID boardId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, schema = @Schema(type = "integer", minimum = "1", example = "20"))
            @RequestParam(name = "size") int size
    ) {
        BoardCursor cursor = null;
        if (createdAt != null && boardId != null) {
            cursor = BoardCursor.of(createdAt, boardId);
        }

        var filter = BoardFilter.empty()
                .withName(name)
                .withDescription(description);

        var query = ListBoardSummaryQuery.of(filter, cursor, size);
        var result = listBoardSummaryUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "게시판 상세 조회", description = "게시판 ID로 게시판 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시판 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = BoardDetail.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시판을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetail> find(
            @Parameter(
                    description = "게시판 ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable("boardId") UUID boardId
    ) {
        var query = FindBoardDetailQuery.of(boardId);
        var result = findBoardDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }
}
