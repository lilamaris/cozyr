package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.port.out.CategoryStore;
import com.lilamaris.cozyr.board.domain.Category;
import com.lilamaris.cozyr.board.persistence.jpa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryStoreJpaAdapter implements CategoryStore {
    private final CategoryRepository repository;

    @Override
    public Category save(Category category) {
        return repository.save(category);
    }

    @Override
    public void delete(Category category) {
        repository.delete(category);
    }
}
