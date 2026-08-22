package com.lilamaris.cozyr.statistics.web.controller;

import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentFilter;
import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentStatistics;
import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostFilter;
import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostStatistics;
import com.lilamaris.cozyr.statistics.application.port.in.FindDailyNewCommentStatisticsUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.FindDailyNewPostStatisticsUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.query.FindDailyNewCommentStatisticsQuery;
import com.lilamaris.cozyr.statistics.application.port.in.query.FindDailyNewPostStatisticsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "통계 API")
public class StatisticsController {
    private final FindDailyNewPostStatisticsUseCase findDailyNewPostStatisticsUseCase;
    private final FindDailyNewCommentStatisticsUseCase findDailyNewCommentStatisticsUseCase;

    @Operation(summary = "게시글 통계 조회", description = "게시판 기준 새 게시글 일별 통계를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = DailyNewPostStatistics.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "새 게시글 통계를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<DailyNewPostStatistics> findDailyNewPostByBoard(
            @Parameter(
                    description = "게시판 ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable("boardId") UUID boardId,
            @Parameter(description = "조회 시작일", required = true, schema = @Schema(type = "string", format = "date", example = "2026-01-01"))
            @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "조회 종료일", required = true, schema = @Schema(type = "string", format = "date", example = "2026-01-31"))
            @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        var filter = DailyNewPostFilter.of(from, to);
        var query = FindDailyNewPostStatisticsQuery.of(boardId, filter);
        var result = findDailyNewPostStatisticsUseCase.findByBoard(query);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "댓글 통계 조회", description = "게시글 기준 새 댓글 일별 통계를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = DailyNewCommentStatistics.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "새 댓글 통계를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<DailyNewCommentStatistics> findDailyNewCommentByPost(
            @Parameter(
                    description = "게시글 ID",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable("postId") long postId,
            @Parameter(description = "조회 시작일", required = true, schema = @Schema(type = "string", format = "date", example = "2026-01-01"))
            @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "조회 종료일", required = true, schema = @Schema(type = "string", format = "date", example = "2026-01-31"))
            @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        var filter = DailyNewCommentFilter.of(from, to);
        var query = FindDailyNewCommentStatisticsQuery.of(postId, filter);
        var result = findDailyNewCommentStatisticsUseCase.find(query);

        return ResponseEntity.ok(result);
    }
}
