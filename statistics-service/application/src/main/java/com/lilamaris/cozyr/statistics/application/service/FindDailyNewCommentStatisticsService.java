package com.lilamaris.cozyr.statistics.application.service;

import com.lilamaris.cozyr.statistics.application.exception.StatisticsServiceProgressCode;
import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentStatistics;
import com.lilamaris.cozyr.statistics.application.port.in.FindDailyNewCommentStatisticsUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.query.FindDailyNewCommentStatisticsQuery;
import com.lilamaris.cozyr.statistics.application.port.out.DailyNewCommentReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindDailyNewCommentStatisticsService implements FindDailyNewCommentStatisticsUseCase {
    private final DailyNewCommentReader reader;

    @Override
    public DailyNewCommentStatistics find(FindDailyNewCommentStatisticsQuery query) {
        var postId = query.postId();
        var filter = query.filter();
        return reader.findPostStatistics(postId, filter.from(), filter.to())
                .orElseThrow(() -> new ApplicationException(StatisticsServiceProgressCode.DAILY_NEW_COMMENT_STATISTICS_NOT_FOUND));
    }
}
