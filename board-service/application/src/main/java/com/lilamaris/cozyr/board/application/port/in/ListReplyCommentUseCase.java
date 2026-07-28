package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.port.in.query.ListReplyCommentQuery;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

public interface ListReplyCommentUseCase {
    CursorResult<CommentDetail, CommentCursor> list(ListReplyCommentQuery query);
}
