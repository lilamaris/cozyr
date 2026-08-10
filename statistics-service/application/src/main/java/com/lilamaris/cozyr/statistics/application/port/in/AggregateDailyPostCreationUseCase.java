package com.lilamaris.cozyr.statistics.application.port.in;

import com.lilamaris.cozyr.statistics.application.port.in.command.AggregateDailyPostCreatedCommand;

public interface AggregateDailyPostCreationUseCase {
    void aggregate(AggregateDailyPostCreatedCommand command);
}
