package com.lilamaris.cozyr.board.web.controller;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.*;
import com.lilamaris.cozyr.board.application.port.in.command.DeleteCommentCommand;
import com.lilamaris.cozyr.board.application.port.in.query.ListReplyCommentQuery;
import com.lilamaris.cozyr.board.application.port.in.query.ListRootCommentQuery;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedCommentResult;
import com.lilamaris.cozyr.board.application.port.in.result.RepliedCommentResult;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedCommentResult;
import com.lilamaris.cozyr.board.web.request.CreateCommentRequest;
import com.lilamaris.cozyr.board.web.request.ReplyCommentRequest;
import com.lilamaris.cozyr.board.web.request.UpdateCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts/{postId}/comments")
@Tag(name = "Comments", description = "댓글 API")
public class CommentController {
    private final CreateCommentUseCase createCommentUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;
    private final ReplyCommentUseCase replyCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;

    private final ListRootCommentUseCase listRootCommentUseCase;
    private final ListReplyCommentUseCase listReplyCommentUseCase;

    @Operation(summary = "댓글 생성", description = "게시글에 루트 댓글을 생성합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 생성 성공",
                    content = @Content(schema = @Schema(implementation = CreatedCommentResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<CreatedCommentResult> create(
            @PathVariable("postId") Long postId,
            @Valid @RequestBody CreateCommentRequest body
    ) {
        var command = body.toCommand(postId);
        var result = createCommentUseCase.create(command);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.commentId())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @Operation(summary = "대댓글 생성", description = "댓글에 대댓글을 생성합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64")),
            @Parameter(name = "commentId", description = "부모 댓글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "대댓글 생성 성공",
                    content = @Content(schema = @Schema(implementation = RepliedCommentResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "부모 댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/{commentId}")
    public ResponseEntity<RepliedCommentResult> reply(
            @PathVariable("commentId") Long parentId,
            @Valid @RequestBody ReplyCommentRequest body
    ) {
        var command = body.toCommand(parentId);
        var result = replyCommentUseCase.reply(command);
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("replies")
                .build()
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @Operation(summary = "댓글 수정", description = "댓글 본문을 수정합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64")),
            @Parameter(name = "commentId", description = "댓글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 수정 성공",
                    content = @Content(schema = @Schema(implementation = UpdatedCommentResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<UpdatedCommentResult> update(
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody UpdateCommentRequest body
    ) {
        var command = body.toCommand(commentId);
        var result = updateCommentUseCase.update(command);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "루트 댓글 목록 조회", description = "게시글의 루트 댓글을 커서 기반으로 조회합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "루트 댓글 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping
    public ResponseEntity<CursorResult<CommentDetail, CommentCursor>> list(
            @PathVariable("postId") Long postId,
            @Parameter(description = "커서 댓글 ID", schema = @Schema(type = "integer", format = "int64"))
            @RequestParam(name = "cid", required = false) Long commentId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, schema = @Schema(type = "integer", minimum = "1", example = "20"))
            @RequestParam(name = "size") int size
    ) {
        CommentCursor cursor = null;
        if (createdAt != null && commentId != null) {
            cursor = CommentCursor.of(createdAt, commentId);
        }

        var query = ListRootCommentQuery.of(postId, cursor, size);
        var result = listRootCommentUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "대댓글 목록 조회", description = "댓글의 대댓글을 커서 기반으로 조회합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64")),
            @Parameter(name = "commentId", description = "부모 댓글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "대댓글 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<CursorResult<CommentDetail, CommentCursor>> listReply(
            @PathVariable("commentId") Long parentId,
            @Parameter(description = "커서 댓글 ID", schema = @Schema(type = "integer", format = "int64"))
            @RequestParam(name = "cid", required = false) Long commentId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, schema = @Schema(type = "integer", minimum = "1", example = "20"))
            @RequestParam(name = "size") int size
    ) {
        CommentCursor cursor = null;
        if (createdAt != null && commentId != null) {
            cursor = CommentCursor.of(createdAt, commentId);
        }

        var query = ListReplyCommentQuery.of(parentId, cursor, size);
        var result = listReplyCommentUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제 상태로 변경합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64")),
            @Parameter(name = "commentId", description = "댓글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable("commentId") Long commentId
    ) {
        var command = DeleteCommentCommand.of(commentId);
        deleteCommentUseCase.delete(command);
        return ResponseEntity.noContent().build();
    }

}
