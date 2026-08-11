package com.lilamaris.cozyr.statistics.application.service;

import com.lilamaris.cozyr.statistics.application.exception.StatisticsServiceProgressCode;
import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostStatistics;
import com.lilamaris.cozyr.statistics.application.port.in.FindDailyNewPostStatisticsUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.query.FindDailyNewPostStatisticsQuery;
import com.lilamaris.cozyr.statistics.application.port.out.DailyNewPostReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindDailyNewPostStatisticsService implements FindDailyNewPostStatisticsUseCase {
    private final DailyNewPostReader reader;

    @Override
    public DailyNewPostStatistics findByBoard(FindDailyNewPostStatisticsQuery query) {
        var boardId = query.boardId();
        var filter = query.filter();
        return reader.findBoardStatistics(boardId, filter.from(), filter.to())
                .orElseThrow(() -> new ApplicationException(StatisticsServiceProgressCode.DAILY_NEW_POST_STATISTICS_NOT_FOUND));
    }
}
