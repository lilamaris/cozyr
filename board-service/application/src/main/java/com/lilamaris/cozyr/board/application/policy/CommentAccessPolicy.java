package com.lilamaris.cozyr.board.application.policy;

import com.lilamaris.cozyr.board.domain.Comment;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommentAccessPolicy {
    public boolean canUpdate(Comment comment, UUID actorUserId) {
        ObjectPrecondition.requireNonNull(comment, "comment");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");

        return comment.getAuthorUserId().equals(actorUserId);
    }

    public boolean canDelete(Comment comment, UUID actorUserId) {
        ObjectPrecondition.requireNonNull(comment, "comment");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");

        return comment.getAuthorUserId().equals(actorUserId);
    }
}
