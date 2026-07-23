package com.lilamaris.cozyr.identity.web.controller;

import com.lilamaris.cozyr.identity.application.port.in.ListJwksUseCase;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/.well-known")
public class WellKnownController {
    private final ListJwksUseCase listJwksUseCase;

    @GetMapping("jwks.json")
    public Map<String, Object> keys() {
        var result = listJwksUseCase.list().stream()
                .map(
                        key -> new RSAKey.Builder(key.publicKey())
                                .keyID(key.kid())
                                .keyUse(KeyUse.SIGNATURE)
                                .algorithm(JWSAlgorithm.RS256)
                                .build()
                )
                .map(RSAKey::toJSONObject)
                .toList();

        return Map.of("keys", result);
    }
}
