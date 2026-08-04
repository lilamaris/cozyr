package com.lilamaris.cozyr.board.contract.event;

import com.lilamaris.cozyr.kernel.message.MessageKind;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BoardServiceMessageKind implements MessageKind {
    POST_CREATED("post.created", 1),
    COMMENT_CREATED("comment.created", 1);

    private final String canonicalName;
    private final int version;

    @Override
    public String canonicalName() {
        return canonicalName;
    }

    @Override
    public int version() {
        return version;
    }
}
