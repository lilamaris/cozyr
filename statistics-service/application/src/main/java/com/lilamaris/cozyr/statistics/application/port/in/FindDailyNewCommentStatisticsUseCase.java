package com.lilamaris.cozyr.statistics.application.port.in;

import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentStatistics;
import com.lilamaris.cozyr.statistics.application.port.in.query.FindDailyNewCommentStatisticsQuery;

public interface FindDailyNewCommentStatisticsUseCase {
    DailyNewCommentStatistics find(FindDailyNewCommentStatisticsQuery query);
}
