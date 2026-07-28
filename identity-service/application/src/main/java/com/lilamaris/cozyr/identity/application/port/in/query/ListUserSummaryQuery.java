package com.lilamaris.cozyr.identity.application.port.in.query;

import com.lilamaris.cozyr.identity.application.model.user.UserCursor;
import com.lilamaris.cozyr.identity.application.model.user.UserFilter;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.jspecify.annotations.Nullable;

public record ListUserSummaryQuery(
        UserFilter filter,
        @Nullable UserCursor cursor,
        int size
) {
    public ListUserSummaryQuery {
        ObjectPrecondition.requireNonNull(filter, "filter");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListUserSummaryQuery of(UserFilter filter, UserCursor cursor, int size) {
        return new ListUserSummaryQuery(filter, cursor, size);
    }
}
