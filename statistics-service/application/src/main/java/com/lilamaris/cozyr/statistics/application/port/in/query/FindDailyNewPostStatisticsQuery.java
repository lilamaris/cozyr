package com.lilamaris.cozyr.statistics.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostFilter;

import java.util.UUID;

public record FindDailyNewPostStatisticsQuery(
        UUID boardId,
        DailyNewPostFilter filter
) {
    public FindDailyNewPostStatisticsQuery {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        ObjectPrecondition.requireNonNull(filter, "filter");
    }

    public static FindDailyNewPostStatisticsQuery of(UUID boardId, DailyNewPostFilter filter) {
        return new FindDailyNewPostStatisticsQuery(boardId, filter);
    }
}
