package com.lilamaris.cozyr.board.application.policy;

import com.lilamaris.cozyr.board.domain.PostReaction;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostReactionAccessPolicy {
    public boolean canDelete(PostReaction postReaction, UUID actorUserId) {
        ObjectPrecondition.requireNonNull(postReaction, "postReaction");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");

        return postReaction.getUserId().equals(actorUserId);
    }
}
