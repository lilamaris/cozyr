package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionSummary;
import com.lilamaris.cozyr.board.application.port.in.query.FindPostReactionSummaryQuery;

public interface FindPostReactionSummaryUseCase {
    PostReactionSummary find(FindPostReactionSummaryQuery query);
}
