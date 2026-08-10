package com.lilamaris.cozyr.statistics.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.LocalDate;

public record AggregateDailyCommentCreatedCommand(
        long postId,
        LocalDate createdDate,
        long createdCount
) {
    public AggregateDailyCommentCreatedCommand {
        NumberPrecondition.requireNonNegative(postId, "postId");
        ObjectPrecondition.requireNonNull(createdDate, "createdDate");
        NumberPrecondition.requireNonNegative(createdCount, "createdCount");
    }

    public static AggregateDailyCommentCreatedCommand of(long postId, LocalDate createdDate, long createdCount) {
        return new AggregateDailyCommentCreatedCommand(postId, createdDate, createdCount);
    }
}
