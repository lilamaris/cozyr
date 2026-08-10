package com.lilamaris.cozyr.statistics.application.service;

import com.lilamaris.cozyr.statistics.application.port.in.AggregateDailyCommentCreationUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.command.AggregateDailyCommentCreatedCommand;
import com.lilamaris.cozyr.statistics.application.port.out.DailyNewCommentStore;
import com.lilamaris.cozyr.statistics.domain.DailyNewComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class AggregateDailyCommentCreationService implements AggregateDailyCommentCreationUseCase {
    private final DailyNewCommentStore store;
    private final Clock clock;

    @Override
    public void aggregate(AggregateDailyCommentCreatedCommand command) {
        var now = clock.instant();

        var postId = command.postId();
        var createdDate = command.createdDate();
        var createdCount = command.createdCount();

        var dailyNewComment = DailyNewComment.of(postId, createdDate, createdCount, now);
        store.upsert(dailyNewComment);
    }
}
