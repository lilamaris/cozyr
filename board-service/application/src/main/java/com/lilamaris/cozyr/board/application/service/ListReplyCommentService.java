package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.port.in.ListReplyCommentUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.ListReplyCommentQuery;
import com.lilamaris.cozyr.board.application.port.out.CommentReader;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import org.springframework.stereotype.Service;

@Service
public class ListReplyCommentService implements ListReplyCommentUseCase {
    private final CommentReader reader;

    public ListReplyCommentService(CommentReader reader) {
        this.reader = reader;
    }

    @Override
    public CursorResult<CommentDetail, CommentCursor> list(ListReplyCommentQuery query) {
        var parentId = query.parentId();
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findReplies(parentId, request);
    }
}
