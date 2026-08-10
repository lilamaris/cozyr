package com.lilamaris.cozyr.statistics.application.port.in;

import com.lilamaris.cozyr.statistics.application.port.in.command.AggregateDailyCommentCreatedCommand;

public interface AggregateDailyCommentCreationUseCase {
    void aggregate(AggregateDailyCommentCreatedCommand command);
}
