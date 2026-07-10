package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.query.ListRootCommentQuery;

public interface ListRootCommentUseCase {
    CursorResult<CommentDetail, CommentCursor> list(ListRootCommentQuery query);
}
