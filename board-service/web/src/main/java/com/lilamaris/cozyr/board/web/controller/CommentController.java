package com.lilamaris.cozyr.board.web.controller;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.*;
import com.lilamaris.cozyr.board.application.port.in.query.ListReplyCommentQuery;
import com.lilamaris.cozyr.board.application.port.in.query.ListRootCommentQuery;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedCommentResult;
import com.lilamaris.cozyr.board.application.port.in.result.RepliedCommentResult;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedCommentResult;
import com.lilamaris.cozyr.board.web.request.CreateCommentRequest;
import com.lilamaris.cozyr.board.web.request.ReplyCommentRequest;
import com.lilamaris.cozyr.board.web.request.UpdateCommentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts/{postId}/comments")
public class CommentController {
    private final CreateCommentUseCase createCommentUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;
    private final ReplyCommentUseCase replyCommentUseCase;

    private final ListRootCommentUseCase listRootCommentUseCase;
    private final ListReplyCommentUseCase listReplyCommentUseCase;

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

    @PutMapping("/{commentId}")
    public ResponseEntity<UpdatedCommentResult> update(
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody UpdateCommentRequest body
    ) {
        var command = body.toCommand(commentId);
        var result = updateCommentUseCase.update(command);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<CursorResult<CommentDetail, CommentCursor>> list(
            @PathVariable("postId") Long postId,
            @RequestParam(name = "cid", required = false) Long commentId,
            @RequestParam(name = "ca", required = false) Instant createdAt,
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

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<CursorResult<CommentDetail, CommentCursor>> listReply(
            @PathVariable("commentId") Long parentId,
            @RequestParam(name = "cid", required = false) Long commentId,
            @RequestParam(name = "ca", required = false) Instant createdAt,
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


}
