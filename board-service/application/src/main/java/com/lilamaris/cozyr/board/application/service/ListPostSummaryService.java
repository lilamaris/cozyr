package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.port.in.ListPostSummaryUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostSummaryQuery;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import org.springframework.stereotype.Service;

@Service
public class ListPostSummaryService implements ListPostSummaryUseCase {
    private final PostReader reader;

    public ListPostSummaryService(PostReader reader) {
        this.reader = reader;
    }

    @Override
    public CursorResult<PostSummary, PostCursor> list(ListPostSummaryQuery query) {
        var filter = query.filter();
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findSummaries(filter, request)
                .map(summary -> summary.truncate(50));
    }
}
