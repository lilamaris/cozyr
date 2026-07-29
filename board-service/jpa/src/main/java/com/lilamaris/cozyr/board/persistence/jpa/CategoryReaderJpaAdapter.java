package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.port.out.CategoryReader;
import com.lilamaris.cozyr.board.domain.Category;
import com.lilamaris.cozyr.board.persistence.jpa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryReaderJpaAdapter implements CategoryReader {
    private final CategoryRepository repository;

    @Override
    public boolean existsById(UUID categoryId) {
        return repository.existsById(categoryId);
    }

    @Override
    public Optional<Category> findById(UUID categoryId) {
        return repository.findById(categoryId);
    }
}
