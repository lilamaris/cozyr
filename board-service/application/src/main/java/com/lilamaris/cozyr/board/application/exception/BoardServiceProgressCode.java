package com.lilamaris.cozyr.board.application.exception;

import com.lilamaris.shrturl.kernel.application.exception.ApplicationProgressCode;
import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BoardServiceProgressCode implements ApplicationProgressCode {
    BOARD_NOT_FOUND(ProcessReason.REJECTED, "board", "not-found", "게시판을 찾을 수 없습니다."),
    POST_NOT_FOUND(ProcessReason.REJECTED, "post", "not-found", "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(ProcessReason.REJECTED, "comment", "not-found", "댓글을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(ProcessReason.REJECTED, "category", "not-found", "카테고리를 찾을 수 없습니다."),
    REACTION_NOT_FOUND(ProcessReason.REJECTED, "reaction", "not-found", "반응을 찾을 수 없습니다."),

    POST_UPDATE_ACCESS_DENIED(ProcessReason.REJECTED, "post", "access-denied", "게시글을 수정할 권한이 없습니다."),
    POST_DELETE_ACCESS_DENIED(ProcessReason.REJECTED, "post", "access-denied", "게시글을 삭제할 권한이 없습니다."),
    COMMENT_UPDATE_ACCESS_DENIED(ProcessReason.REJECTED, "comment", "access-denied", "댓글을 수정할 권한이 없습니다."),
    COMMENT_DELETE_ACCESS_DENIED(ProcessReason.REJECTED, "comment", "access-denied", "댓글을 삭제할 권한이 없습니다."),
    REACTION_DELETE_ACCESS_DENIED(ProcessReason.REJECTED, "reaction", "access-denied", "반응을 삭제할 권한이 없습니다."),

    REACTION_DUPLICATED(ProcessReason.REJECTED, "reaction", "duplicated", "이미 반응했습니다.");

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
