package com.lilamaris.cozyr.board.application.exception;

import com.lilamaris.shrturl.kernel.application.exception.ApplicationProgressCode;
import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BoardServiceProgressCode implements ApplicationProgressCode {
    BOARD_NOT_FOUND(ProcessReason.REJECTED, "board", "not-found", "게시판을 찾을 수 없습니다."),
    POST_NOT_FOUND(ProcessReason.REJECTED, "post", "not-found", "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(ProcessReason.REJECTED, "comment", "not-found", "댓글을 찾을 수 없습니다.");

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
