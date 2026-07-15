package com.lilamaris.cozyr.identity.jpa;

import com.lilamaris.cozyr.identity.application.port.out.CredentialReader;
import com.lilamaris.cozyr.identity.application.port.out.CredentialStore;
import com.lilamaris.cozyr.identity.domain.Credential;
import com.lilamaris.cozyr.identity.jpa.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CredentialJpaAdapter implements CredentialReader, CredentialStore {
    private final CredentialRepository repository;

    @Override
    public Optional<Credential> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Credential> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Credential save(Credential credential) {
        return repository.save(credential);
    }
}
