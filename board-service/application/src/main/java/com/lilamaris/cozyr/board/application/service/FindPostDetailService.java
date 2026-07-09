package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.port.in.FindPostDetailUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.FindPostDetailQuery;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;

@Service
public class FindPostDetailService implements FindPostDetailUseCase {
    private final PostReader reader;

    public FindPostDetailService(PostReader reader) {
        this.reader = reader;
    }

    @Override
    public PostDetail find(FindPostDetailQuery query) {
        var postId = query.postId();
        return reader.findDetailById(postId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.POST_NOT_FOUND));
    }
}
