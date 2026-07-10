package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.domain.Comment;

public interface CommentStore {
    Comment save(Comment comment);
}
