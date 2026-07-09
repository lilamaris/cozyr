package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostSummaryQuery;

public interface ListPostSummaryUseCase {
    CursorResult<PostSummary, PostCursor> list(ListPostSummaryQuery query);
}
