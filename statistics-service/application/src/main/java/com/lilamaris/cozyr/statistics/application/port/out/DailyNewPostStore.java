package com.lilamaris.cozyr.statistics.application.port.out;

import com.lilamaris.cozyr.statistics.domain.DailyNewPost;

public interface DailyNewPostStore {
    void upsert(DailyNewPost dailyNewPost);
}
