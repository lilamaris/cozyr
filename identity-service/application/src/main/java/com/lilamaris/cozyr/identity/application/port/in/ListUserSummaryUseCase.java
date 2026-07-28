package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.model.user.UserCursor;
import com.lilamaris.cozyr.identity.application.model.user.UserSummary;
import com.lilamaris.cozyr.identity.application.port.in.query.ListUserSummaryQuery;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;

public interface ListUserSummaryUseCase {
    CursorResult<UserSummary, UserCursor> list(ListUserSummaryQuery query);
}
