package com.lilamaris.cozyr.statistics.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentFilter;

public record FindDailyNewCommentStatisticsQuery(
        long postId,
        DailyNewCommentFilter filter
) {
    public FindDailyNewCommentStatisticsQuery {
        NumberPrecondition.requireNonNegative(postId, "postId");
        ObjectPrecondition.requireNonNull(filter, "filter");
    }

    public static FindDailyNewCommentStatisticsQuery of(long postId, DailyNewCommentFilter filter) {
        return new FindDailyNewCommentStatisticsQuery(postId, filter);
    }
}
