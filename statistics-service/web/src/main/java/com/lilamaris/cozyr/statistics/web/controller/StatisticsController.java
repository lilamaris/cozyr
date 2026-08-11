package com.lilamaris.cozyr.statistics.web.controller;

import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentFilter;
import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentStatistics;
import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostFilter;
import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostStatistics;
import com.lilamaris.cozyr.statistics.application.port.in.FindDailyNewCommentStatisticsUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.FindDailyNewPostStatisticsUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.query.FindDailyNewCommentStatisticsQuery;
import com.lilamaris.cozyr.statistics.application.port.in.query.FindDailyNewPostStatisticsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final FindDailyNewPostStatisticsUseCase findDailyNewPostStatisticsUseCase;
    private final FindDailyNewCommentStatisticsUseCase findDailyNewCommentStatisticsUseCase;

    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<DailyNewPostStatistics> findDailyNewPostByBoard(
            @PathVariable("boardId") UUID boardId,
            @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        var filter = DailyNewPostFilter.of(from, to);
        var query = FindDailyNewPostStatisticsQuery.of(boardId, filter);
        var result = findDailyNewPostStatisticsUseCase.findByBoard(query);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<DailyNewCommentStatistics> findDailyNewCommentByPost(
            @PathVariable("postId") long postId,
            @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        var filter = DailyNewCommentFilter.of(from, to);
        var query = FindDailyNewCommentStatisticsQuery.of(postId, filter);
        var result = findDailyNewCommentStatisticsUseCase.find(query);

        return ResponseEntity.ok(result);
    }
}
