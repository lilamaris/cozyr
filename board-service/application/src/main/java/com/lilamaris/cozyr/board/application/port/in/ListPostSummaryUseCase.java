package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostSummaryQuery;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

public interface ListPostSummaryUseCase {
    CursorResult<PostSummary, PostCursor> list(ListPostSummaryQuery query);
}
