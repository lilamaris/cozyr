package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionActivity;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostReactionActivityQuery;

import java.util.List;

public interface ListPostReactionActivityUseCase {
    List<PostReactionActivity> list(ListPostReactionActivityQuery query);
}
