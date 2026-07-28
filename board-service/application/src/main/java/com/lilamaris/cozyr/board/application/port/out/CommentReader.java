package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.model.comment.CommentFilter;
import com.lilamaris.cozyr.board.domain.Comment;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

import java.util.Optional;

public interface CommentReader {
    Optional<Comment> findById(Long id);

    CursorResult<CommentDetail, CommentCursor> findByPostId(CommentFilter filter, CursorRequest<CommentCursor> request);
}
