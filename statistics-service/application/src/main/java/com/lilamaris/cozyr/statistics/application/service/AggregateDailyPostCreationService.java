package com.lilamaris.cozyr.statistics.application.service;

import com.lilamaris.cozyr.statistics.application.port.in.AggregateDailyPostCreationUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.command.AggregateDailyPostCreatedCommand;
import com.lilamaris.cozyr.statistics.application.port.out.DailyNewPostStore;
import com.lilamaris.cozyr.statistics.domain.DailyNewPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class AggregateDailyPostCreationService implements AggregateDailyPostCreationUseCase {
    private final DailyNewPostStore store;
    private final Clock clock;

    @Override
    public void aggregate(AggregateDailyPostCreatedCommand command) {
        var now = clock.instant();
        var createdDate = command.createdDate();
        var createdCount = command.createdCount();
        var boardId = command.boardId();

        var dailyNewPost = DailyNewPost.of(boardId, createdDate, createdCount, now);
        store.upsert(dailyNewPost);
    }
}
