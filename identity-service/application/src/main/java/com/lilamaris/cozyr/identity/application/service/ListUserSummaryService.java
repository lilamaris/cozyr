package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.model.user.UserCursor;
import com.lilamaris.cozyr.identity.application.model.user.UserSummary;
import com.lilamaris.cozyr.identity.application.port.in.ListUserSummaryUseCase;
import com.lilamaris.cozyr.identity.application.port.in.query.ListUserSummaryQuery;
import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListUserSummaryService implements ListUserSummaryUseCase {
    private final UserReader reader;

    @Override
    public CursorResult<UserSummary, UserCursor> list(ListUserSummaryQuery query) {
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findSummaries(query.filter(), request);
    }
}
