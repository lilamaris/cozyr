package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.port.in.query.FindPostDetailQuery;

public interface FindPostDetailUseCase {
    PostDetail find(FindPostDetailQuery query);
}
