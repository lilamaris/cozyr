package com.lilamaris.cozyr.board.application.port.out;

import java.time.Instant;

public interface PostViewCounter {
    void increase(long postId, Instant viewedAt);
}
