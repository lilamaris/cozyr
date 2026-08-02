package com.lilamaris.cozyr.board.web.controller;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.model.comment.CommentFilter;
import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostFilter;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionActivity;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionCursor;
import com.lilamaris.cozyr.board.application.port.in.ListCommentDetailUseCase;
import com.lilamaris.cozyr.board.application.port.in.ListPostReactionActivityUseCase;
import com.lilamaris.cozyr.board.application.port.in.ListPostSummaryUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.ListCommentDetailQuery;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostReactionActivityQuery;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostSummaryQuery;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/boards/activities")
@RequiredArgsConstructor
@Tag(name = "Activities", description = "사용자 활동 API")
public class ActivityController {
    private final ListPostSummaryUseCase listPostSummaryUseCase;
    private final ListCommentDetailUseCase listCommentDetailUseCase;
    private final ListPostReactionActivityUseCase listPostReactionActivityUseCase;

    private final IdentityContextHolder identityContextHolder;

    @Operation(summary = "사용자 게시글 활동 목록 조회", description = "사용자가 작성한 게시글을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 게시글 활동 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/posts")
    public ResponseEntity<CursorResult<PostSummary, PostCursor>> listUserPost(
            @Parameter(description = "사용자 ID", required = true, schema = @Schema(type = "string", format = "uuid"))
            @RequestParam(name = "uid") UUID userId,
            @Parameter(description = "커서 게시글 ID", schema = @Schema(type = "integer", format = "int64"))
            @RequestParam(name = "pid", required = false) Long postId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, example = "20", schema = @Schema(type = "integer", minimum = "1"))
            @RequestParam(name = "size") int size
    ) {
        PostCursor cursor = null;
        if (createdAt != null && postId != null) {
            cursor = PostCursor.of(createdAt, postId);
        }

        var filter = PostFilter.empty()
                .withAuthorUserId(userId);

        var query = ListPostSummaryQuery.of(filter, cursor, size);
        var result = listPostSummaryUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "내 게시글 활동 목록 조회", description = "인증된 사용자가 작성한 게시글을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "내 게시글 활동 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/posts/me")
    public ResponseEntity<CursorResult<PostSummary, PostCursor>> listMePost(
            @Parameter(description = "커서 게시글 ID", schema = @Schema(type = "integer", format = "int64"))
            @RequestParam(name = "pid", required = false) Long postId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, example = "20", schema = @Schema(type = "integer", minimum = "1"))
            @RequestParam(name = "size") int size
    ) {
        PostCursor cursor = null;
        if (createdAt != null && postId != null) {
            cursor = PostCursor.of(createdAt, postId);
        }

        var identity = identityContextHolder.get();
        var filter = PostFilter.empty()
                .withAuthorUserId(identity.id());

        var query = ListPostSummaryQuery.of(filter, cursor, size);
        var result = listPostSummaryUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "내 게시글 반응 활동 목록 조회", description = "인증된 사용자가 반응한 게시글을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "내 게시글 반응 활동 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostReactionActivity.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/posts/reactions/me")
    public ResponseEntity<List<PostReactionActivity>> listMePostReactionActivity(
            @Parameter(description = "커서 게시글 ID", schema = @Schema(type = "integer", format = "int64"))
            @RequestParam(name = "pid", required = false) Long postId,
            @Parameter(description = "커서 반응 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant reactedAt,
            @Parameter(description = "조회 개수", required = true, example = "20", schema = @Schema(type = "integer", minimum = "1"))
            @RequestParam(name = "size") int size
    ) {
        var identity = identityContextHolder.get();
        PostReactionCursor cursor = null;
        if (reactedAt != null && postId != null) {
            cursor = PostReactionCursor.of(reactedAt, postId);
        }
        var query = ListPostReactionActivityQuery.of(identity.id(), cursor, size);
        var result = listPostReactionActivityUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "사용자 댓글 활동 목록 조회", description = "사용자가 작성한 댓글을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 댓글 활동 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/comments")
    public ResponseEntity<CursorResult<CommentDetail, CommentCursor>> listUserComment(
            @Parameter(description = "사용자 ID", required = true, schema = @Schema(type = "string", format = "uuid"))
            @RequestParam(name = "uid") UUID userId,
            @Parameter(description = "커서 댓글 ID", schema = @Schema(type = "integer", format = "int64"))
            @RequestParam(name = "cid", required = false) Long commentId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, example = "20", schema = @Schema(type = "integer", minimum = "1"))
            @RequestParam(name = "size") int size
    ) {
        CommentCursor cursor = null;
        if (createdAt != null && commentId != null) {
            cursor = CommentCursor.of(createdAt, commentId);
        }

        var filter = CommentFilter.empty()
                .withAuthorUserId(userId);

        var query = ListCommentDetailQuery.of(filter, cursor, size);
        var result = listCommentDetailUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "내 댓글 활동 목록 조회", description = "인증된 사용자가 작성한 댓글을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "내 댓글 활동 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/comments/me")
    public ResponseEntity<CursorResult<CommentDetail, CommentCursor>> listMeComment(
            @Parameter(description = "커서 댓글 ID", schema = @Schema(type = "integer", format = "int64"))
            @RequestParam(name = "cid", required = false) Long commentId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, example = "20", schema = @Schema(type = "integer", minimum = "1"))
            @RequestParam(name = "size") int size
    ) {
        CommentCursor cursor = null;
        if (createdAt != null && commentId != null) {
            cursor = CommentCursor.of(createdAt, commentId);
        }

        var identity = identityContextHolder.get();
        var filter = CommentFilter.empty()
                .withAuthorUserId(identity.id());

        var query = ListCommentDetailQuery.of(filter, cursor, size);
        var result = listCommentDetailUseCase.list(query);
        return ResponseEntity.ok(result);
    }
}
