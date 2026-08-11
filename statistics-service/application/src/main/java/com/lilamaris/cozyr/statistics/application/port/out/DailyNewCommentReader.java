package com.lilamaris.cozyr.statistics.application.port.out;

import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentStatistics;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyNewCommentReader {
    Optional<DailyNewCommentStatistics> findPostStatistics(long postId, LocalDate from, LocalDate to);
}
