package com.lilamaris.cozyr.board.web.controller;

import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.model.post.PostFilter;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.port.in.*;
import com.lilamaris.cozyr.board.application.port.in.command.CancelReactPostCommand;
import com.lilamaris.cozyr.board.application.port.in.command.DeletePostCommand;
import com.lilamaris.cozyr.board.application.port.in.query.FindPostDetailQuery;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostSummaryQuery;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedPostResult;
import com.lilamaris.cozyr.board.application.port.in.result.ReactedPostResult;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedPostResult;
import com.lilamaris.cozyr.board.web.request.CreatePostRequest;
import com.lilamaris.cozyr.board.web.request.ReactPostRequest;
import com.lilamaris.cozyr.board.web.request.UpdatePostRequest;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts")
@Tag(name = "Posts", description = "게시글 API")
public class PostController {
    private final CreatePostUseCase createPostUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final ReactPostUseCase reactPostUseCase;
    private final CancelReactPostUseCase cancelReactPostUseCase;

    private final FindPostDetailUseCase findPostDetailUseCase;
    private final ListPostSummaryUseCase listPostSummaryUseCase;

    private final IdentityContextHolder identityContextHolder;

    @Operation(summary = "게시글 생성", description = "게시판 카테고리에 새 게시글을 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "게시글 생성 성공",
                    content = @Content(schema = @Schema(implementation = CreatedPostResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시판 또는 카테고리를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<CreatedPostResult> create(
            @Parameter(
                    description = "게시판 ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable("boardId") UUID boardId,
            @Valid @RequestBody CreatePostRequest body
    ) {
        var identity = identityContextHolder.get();
        var command = body.toCommand(boardId, identity.id());
        var result = createPostUseCase.create(command);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.postId())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @Operation(summary = "게시글 반응", description = "게시글에 반응을 추가합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 반응 성공",
                    content = @Content(schema = @Schema(implementation = ReactedPostResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/{postId}/reactions")
    public ResponseEntity<ReactedPostResult> reaction(
            @PathVariable("postId") long postId,
            @Valid @RequestBody ReactPostRequest body
    ) {
        var identity = identityContextHolder.get();
        var command = body.toCommand(postId, identity.id());
        var result = reactPostUseCase.react(command);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "게시글 수정", description = "게시글 카테고리, 제목과 본문을 수정합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 수정 성공",
                    content = @Content(schema = @Schema(implementation = UpdatedPostResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글 또는 카테고리를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PutMapping("/{postId}")
    public ResponseEntity<UpdatedPostResult> update(
            @PathVariable("postId") long postId,
            @Valid @RequestBody UpdatePostRequest body
    ) {
        var identity = identityContextHolder.get();
        var command = body.toCommand(postId, identity.id());
        var result = updatePostUseCase.update(command);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "게시글 목록 조회", description = "게시판의 게시글을 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping
    public ResponseEntity<CursorResult<PostSummary, PostCursor>> list(
            @Parameter(
                    description = "게시판 ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable("boardId") UUID boardId,
            @Parameter(description = "제목 검색어", schema = @Schema(type = "string", example = "공지"))
            @RequestParam(name = "title", required = false) String title,
            @Parameter(description = "본문 검색어", schema = @Schema(type = "string", example = "이벤트"))
            @RequestParam(name = "content", required = false) String content,
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
                .withBoardId(boardId)
                .withTitle(title)
                .withContent(content);

        var query = ListPostSummaryQuery.of(filter, cursor, size);
        var result = listPostSummaryUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 ID로 게시글 상세 정보를 조회합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostDetail.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetail> find(
            @PathVariable("postId") Long postId
    ) {
        var query = FindPostDetailQuery.of(postId);
        var result = findPostDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제 상태로 변경합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
    })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable("postId") Long postId
    ) {
        var identity = identityContextHolder.get();
        var command = DeletePostCommand.of(postId, identity.id());
        deletePostUseCase.delete(command);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 반응 취소", description = "게시글에 추가한 반응을 취소합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "postId", description = "게시글 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64")),
            @Parameter(name = "reactionId", description = "반응 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid"))
    })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 반응 취소 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "반응을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{postId}/reactions/{reactionId}")
    public ResponseEntity<Void> cancelReaction(
            @PathVariable("reactionId") UUID reactionId
    ) {
        var identity = identityContextHolder.get();
        var command = CancelReactPostCommand.of(reactionId, identity.id());
        cancelReactPostUseCase.cancel(command);
        return ResponseEntity.noContent().build();
    }
}
