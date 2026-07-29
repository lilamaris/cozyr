package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.port.in.CreatePostUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CreatePostCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedPostResult;
import com.lilamaris.cozyr.board.application.port.out.BoardReader;
import com.lilamaris.cozyr.board.application.port.out.CategoryReader;
import com.lilamaris.cozyr.board.application.port.out.PostStore;
import com.lilamaris.cozyr.board.domain.Post;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class CreatePostService implements CreatePostUseCase {
    private final BoardReader boardReader;
    private final CategoryReader categoryReader;
    private final PostStore store;
    private final Clock clock;

    public CreatePostService(BoardReader boardReader, CategoryReader categoryReader, PostStore store, Clock clock) {
        this.boardReader = boardReader;
        this.categoryReader = categoryReader;
        this.store = store;
        this.clock = clock;
    }

    @Override
    @Transactional
    public CreatedPostResult create(CreatePostCommand command) {
        var now = clock.instant();
        var boardId = command.boardId();
        if (!boardReader.existsById(boardId))
            throw new ApplicationException(BoardServiceProgressCode.BOARD_NOT_FOUND);

        var categoryId = command.categoryId();
        if (!categoryReader.existsById(categoryId))
            throw new ApplicationException(BoardServiceProgressCode.CATEGORY_NOT_FOUND);

        var title = command.title();
        var content = command.content();
        var authorUserId = command.authorUserId();

        var post = Post.of(boardId, categoryId, title, content, now, authorUserId);
        var saved = store.save(post);

        return CreatedPostResult.from(saved);
    }
}
