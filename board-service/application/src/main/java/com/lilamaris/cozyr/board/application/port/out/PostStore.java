package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.domain.Post;

public interface PostStore {
    Post save(Post post);
}
