package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionActivity;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionCursor;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionFilter;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionSummary;
import com.lilamaris.cozyr.board.domain.PostReaction;
import com.lilamaris.cozyr.board.domain.ReactionType;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostReactionReader {
    boolean existsReaction(long postId, UUID userId, ReactionType type);

    Optional<PostReaction> findById(UUID id);

    Optional<PostReactionSummary> findSummaries(PostReactionFilter filter);

    List<PostReactionActivity> findActivities(UUID userId, CursorRequest<PostReactionCursor> request);
}
