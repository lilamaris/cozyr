package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionCursor;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record ListPostReactionActivityQuery(
        UUID userId,
        @Nullable PostReactionCursor cursor,
        int size
) {
    public ListPostReactionActivityQuery {
        ObjectPrecondition.requireNonNull(userId, "userId");
        NumberPrecondition.requirePositive(size, "size");
    }

    public static ListPostReactionActivityQuery of(UUID userId, @Nullable PostReactionCursor cursor, int size) {
        return new ListPostReactionActivityQuery(userId, cursor, size);
    }
}
