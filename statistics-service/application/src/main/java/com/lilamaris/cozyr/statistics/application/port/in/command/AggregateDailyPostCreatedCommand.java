package com.lilamaris.cozyr.statistics.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.LocalDate;
import java.util.UUID;

public record AggregateDailyPostCreatedCommand(
        UUID boardId,
        LocalDate createdDate,
        long createdCount
) {
    public AggregateDailyPostCreatedCommand {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        ObjectPrecondition.requireNonNull(createdDate, "createdDate");
        NumberPrecondition.requireNonNegative(createdCount, "createdCount");
    }

    public static AggregateDailyPostCreatedCommand of(UUID boardId, LocalDate createdDate, long createdCount) {
        return new AggregateDailyPostCreatedCommand(boardId, createdDate, createdCount);
    }
}
