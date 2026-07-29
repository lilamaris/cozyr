package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.policy.PostAccessPolicy;
import com.lilamaris.cozyr.board.application.port.in.UpdatePostUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.UpdatePostCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedPostResult;
import com.lilamaris.cozyr.board.application.port.out.CategoryReader;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class UpdatePostService implements UpdatePostUseCase {
    private final CategoryReader categoryReader;
    private final PostReader reader;
    private final PostAccessPolicy policy;
    private final Clock clock;

    public UpdatePostService(CategoryReader categoryReader, PostReader reader, PostAccessPolicy policy, Clock clock) {
        this.categoryReader = categoryReader;
        this.reader = reader;
        this.policy = policy;
        this.clock = clock;
    }

    @Override
    @Transactional
    public UpdatedPostResult update(UpdatePostCommand command) {
        var postId = command.postId();
        var post = reader.findById(postId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.POST_NOT_FOUND));

        var categoryId = command.categoryId();
        if (!categoryReader.existsById(categoryId))
            throw new ApplicationException(BoardServiceProgressCode.CATEGORY_NOT_FOUND);

        var actorUserId = command.actorUserId();
        if (!policy.canUpdate(post, actorUserId))
            throw new ApplicationException(BoardServiceProgressCode.POST_UPDATE_ACCESS_DENIED);

        var now = clock.instant();
        var title = command.title();
        var content = command.content();

        post.updateCategoryId(categoryId, now);
        post.updateTitle(title, now);
        post.updateContent(content, now);

        return UpdatedPostResult.from(post);
    }
}
