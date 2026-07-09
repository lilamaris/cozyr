package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.port.in.CreatePostUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CreatePostCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedPostResult;
import com.lilamaris.cozyr.board.application.port.out.PostStore;
import com.lilamaris.cozyr.board.domain.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class CreatePostService implements CreatePostUseCase {
    private final PostStore store;
    private final Clock clock;

    public CreatePostService(PostStore store, Clock clock) {
        this.store = store;
        this.clock = clock;
    }

    @Override
    @Transactional
    public CreatedPostResult create(CreatePostCommand command) {
        var now = clock.instant();
        var boardId = command.boardId();
        var title = command.title();
        var content = command.content();

        var post = Post.of(boardId, title, content, now);
        var saved = store.save(post);

        return CreatedPostResult.from(saved);
    }
}
