package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.application.model.cursor.CursorRequest;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.model.post.PostFilter;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.domain.Post;

import java.util.Optional;
import java.util.UUID;

public interface PostReader {
    Optional<Post> findById(long id);

    Optional<PostDetail> findDetailById(long id);

    CursorResult<PostSummary, PostCursor> findSummaries(UUID boardId, PostFilter filter, CursorRequest<PostCursor> request);
}
