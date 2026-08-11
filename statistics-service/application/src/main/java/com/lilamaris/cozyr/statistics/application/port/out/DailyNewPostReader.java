package com.lilamaris.cozyr.statistics.application.port.out;

import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostStatistics;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DailyNewPostReader {
    Optional<DailyNewPostStatistics> findBoardStatistics(UUID boardId, LocalDate from, LocalDate to);
}
