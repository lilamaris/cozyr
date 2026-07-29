package com.lilamaris.cozyr.board.application.policy;

import com.lilamaris.cozyr.board.domain.Post;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostAccessPolicy {
    public boolean canUpdate(Post post, UUID actorUserId) {
        ObjectPrecondition.requireNonNull(post, "post");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");

        return post.getAuthorUserId().equals(actorUserId);
    }

    public boolean canDelete(Post post, UUID actorUserId) {
        ObjectPrecondition.requireNonNull(post, "post");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");

        return post.getAuthorUserId().equals(actorUserId);
    }
}
