package com.lilamaris.cozyr.statistics.application.exception;

import com.lilamaris.shrturl.kernel.application.exception.ApplicationProgressCode;
import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatisticsServiceProgressCode implements ApplicationProgressCode {
    DAILY_NEW_POST_STATISTICS_NOT_FOUND(ProcessReason.REJECTED, "daily-new-post", "not-found", "새 게시글 통계를 찾을 수 없습니다."),
    DAILY_NEW_COMMENT_STATISTICS_NOT_FOUND(ProcessReason.REJECTED, "daily-new-comment", "not-found", "새 댓글 통계를 찾을 수 없습니다.");

    private final ProcessReason reason;
    private final String resourceName;
    private final String type;
    private final String message;

    @Override
    public ProcessReason reason() {
        return reason;
    }

    @Override
    public String resourceName() {
        return resourceName;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String message() {
        return message;
    }
}
