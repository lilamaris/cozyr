package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.port.in.FindPostDetailUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.FindPostDetailQuery;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.cozyr.board.application.port.out.PostViewCounter;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class FindPostDetailService implements FindPostDetailUseCase {
    private final PostReader reader;
    private final PostViewCounter postViewCounter;
    private final Clock clock;

    @Override
    public PostDetail find(FindPostDetailQuery query) {
        var postId = query.postId();
        var post = reader.findDetailById(postId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.POST_NOT_FOUND));

        var now = clock.instant();
        postViewCounter.increase(postId, now);

        return post;
    }
}
