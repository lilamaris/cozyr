package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.domain.PostReaction;

public interface PostReactionStore {
    PostReaction save(PostReaction postReaction);

    void delete(PostReaction postReaction);
}
