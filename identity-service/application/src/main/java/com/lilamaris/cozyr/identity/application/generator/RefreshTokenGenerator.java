package com.lilamaris.cozyr.identity.application.generator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.random.RandomGenerator;

@Component
@RequiredArgsConstructor
public class RefreshTokenGenerator {
    private final RandomGenerator generator;
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public String generate() {
        byte[] randomBytes = new byte[32];
        generator.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}
