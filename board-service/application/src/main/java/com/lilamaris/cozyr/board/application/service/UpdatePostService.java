package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.UpdatePostUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.UpdatePostCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedPostResult;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class UpdatePostService implements UpdatePostUseCase {
    private final PostReader reader;
    private final Clock clock;

    public UpdatePostService(PostReader reader, Clock clock) {
        this.reader = reader;
        this.clock = clock;
    }

    @Override
    @Transactional
    public UpdatedPostResult update(UpdatePostCommand command) {
        var postId = command.postId();
        var post = reader.findById(postId)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.POST_NOT_FOUND));

        var now = clock.instant();
        var title = command.title();
        var content = command.content();
        post.updateTitle(title, now);
        post.updateContent(content, now);

        return UpdatedPostResult.from(post);
    }
}
