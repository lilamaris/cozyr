package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.port.in.ListCommentDetailUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.ListCommentDetailQuery;
import com.lilamaris.cozyr.board.application.port.out.CommentReader;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import org.springframework.stereotype.Service;

@Service
public class ListCommentDetailService implements ListCommentDetailUseCase {
    private final CommentReader reader;

    public ListCommentDetailService(CommentReader reader) {
        this.reader = reader;
    }

    @Override
    public CursorResult<CommentDetail, CommentCursor> list(ListCommentDetailQuery query) {
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findByPostId(query.filter(), request);
    }
}
