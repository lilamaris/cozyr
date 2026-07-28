package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.port.in.ListRootCommentUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.ListRootCommentQuery;
import com.lilamaris.cozyr.board.application.port.out.CommentReader;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import org.springframework.stereotype.Service;

@Service
public class ListRootCommentService implements ListRootCommentUseCase {
    private final CommentReader reader;

    public ListRootCommentService(CommentReader reader) {
        this.reader = reader;
    }

    @Override
    public CursorResult<CommentDetail, CommentCursor> list(ListRootCommentQuery query) {
        var postId = query.postId();
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findByPostId(postId, request);
    }
}
