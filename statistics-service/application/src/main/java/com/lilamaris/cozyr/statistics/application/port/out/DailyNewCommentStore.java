package com.lilamaris.cozyr.statistics.application.port.out;

import com.lilamaris.cozyr.statistics.domain.DailyNewComment;

public interface DailyNewCommentStore {
    void upsert(DailyNewComment dailyNewComment);
}
