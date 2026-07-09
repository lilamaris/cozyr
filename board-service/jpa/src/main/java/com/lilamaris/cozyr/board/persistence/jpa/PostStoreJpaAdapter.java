package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.port.out.PostStore;
import com.lilamaris.cozyr.board.domain.Post;
import com.lilamaris.cozyr.board.persistence.jpa.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostStoreJpaAdapter implements PostStore {
    private final PostRepository repository;

    @Override
    public Post save(Post post) {
        return repository.save(post);
    }
}
