package com.lilamaris.cozyr.statistics.application.port.in;

import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostStatistics;
import com.lilamaris.cozyr.statistics.application.port.in.query.FindDailyNewPostStatisticsQuery;

public interface FindDailyNewPostStatisticsUseCase {
    DailyNewPostStatistics findByBoard(FindDailyNewPostStatisticsQuery query);
}
