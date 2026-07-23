package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.model.RSAKeyPair;
import com.lilamaris.cozyr.identity.application.port.in.ListJwksUseCase;
import com.lilamaris.cozyr.identity.application.port.out.JwksReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListJwksService implements ListJwksUseCase {
    private final JwksReader reader;

    @Override
    public List<RSAKeyPair> list() {
        return reader.findVerifiableKeys().stream()
                .map(RSAKeyPair::toVerifiable)
                .toList();
    }
}
