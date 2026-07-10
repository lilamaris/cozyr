package com.lilamaris.cozyr.board.web.controller;

import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.model.post.PostFilter;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.port.in.*;
import com.lilamaris.cozyr.board.application.port.in.command.DeletePostCommand;
import com.lilamaris.cozyr.board.application.port.in.query.FindPostDetailQuery;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostSummaryQuery;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedPostResult;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedPostResult;
import com.lilamaris.cozyr.board.web.request.CreatePostRequest;
import com.lilamaris.cozyr.board.web.request.UpdatePostRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts")
public class PostController {
    private final CreatePostUseCase createPostUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final DeletePostUseCase deletePostUseCase;

    private final FindPostDetailUseCase findPostDetailUseCase;
    private final ListPostSummaryUseCase listPostSummaryUseCase;

    @PostMapping
    public ResponseEntity<CreatedPostResult> create(
            @PathVariable("boardId") UUID boardId,
            @Valid @RequestBody CreatePostRequest body
    ) {
        var command = body.toCommand(boardId);
        var result = createPostUseCase.create(command);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.postId())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<UpdatedPostResult> update(
            @PathVariable("postId") long postId,
            @Valid @RequestBody UpdatePostRequest body
    ) {
        var command = body.toCommand(postId);
        var result = updatePostUseCase.update(command);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<CursorResult<PostSummary, PostCursor>> list(
            @PathVariable("boardId") UUID boardId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "content", required = false) String content,
            @RequestParam(name = "pid", required = false) Long postId,
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @RequestParam(name = "size") int size
    ) {
        PostCursor cursor = null;
        if (createdAt != null && postId != null) {
            cursor = PostCursor.of(createdAt, postId);
        }

        var filter = PostFilter.empty()
                .withTitle(title)
                .withContent(content);

        var query = ListPostSummaryQuery.of(boardId, filter, cursor, size);
        var result = listPostSummaryUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetail> find(
            @PathVariable("postId") Long postId
    ) {
        var query = FindPostDetailQuery.of(postId);
        var result = findPostDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable("postId") Long postId
    ) {
        var command = DeletePostCommand.of(postId);
        deletePostUseCase.delete(command);
        return ResponseEntity.noContent().build();
    }
}
