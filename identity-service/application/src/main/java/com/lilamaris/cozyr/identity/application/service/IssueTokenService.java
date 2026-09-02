package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.config.ApplicationProperties;
import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.generator.RefreshTokenGenerator;
import com.lilamaris.cozyr.identity.application.model.token.TokenItem;
import com.lilamaris.cozyr.identity.application.port.in.IssueTokenUseCase;
import com.lilamaris.cozyr.identity.application.port.in.result.TokenResult;
import com.lilamaris.cozyr.identity.application.port.out.PrincipalReader;
import com.lilamaris.cozyr.identity.contract.codec.ScopeCodec;
import com.lilamaris.cozyr.identity.contract.schema.Scope;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueTokenService implements IssueTokenUseCase {
    private final PrincipalReader principalReader;
    private final ApplicationProperties properties;
    private final ScopeCodec scopeCodec;
    private final JwtEncoder jwtEncoder;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public TokenResult issue(UUID userId) {
        var principal = principalReader.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(IdentityServiceProgressCode.USER_NOT_FOUND));

        var displayName = principal.displayName();
        var version = principal.version();
        var scopes = principal.scopes();

        var accessTokenProperties = properties.accessToken();
        var accessTokenExpiration = accessTokenProperties.expiration();
        var accessToken = buildAccessToken(userId, displayName, version, scopes, accessTokenExpiration);

        var refreshTokenProperties = properties.refreshToken();
        var refreshTokenExpiration = refreshTokenProperties.expiration();
        var refreshToken = buildRefreshToken(refreshTokenExpiration);

        return TokenResult.of(accessToken, refreshToken);
    }

    private TokenItem buildAccessToken(UUID userId, String displayName, long version, Set<Scope> scopes, Duration expiration) {
        var now = clock.instant();
        var expiresAt = now.plus(expiration);
        var encoded = scopeCodec.encode(scopes);

        var claims = JwtClaimsSet.builder()
                .issuer(properties.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userId.toString())
                .claim("scopes", encoded)
                .claim("displayName", displayName)
                .claim("version", version)
                .build();
        var parameters = JwtEncoderParameters.from(claims);
        var value = jwtEncoder.encode(parameters).getTokenValue();

        return TokenItem.of(value, expiration.toSeconds());
    }

    private TokenItem buildRefreshToken(Duration expiration) {
        var value = refreshTokenGenerator.generate();

        return TokenItem.of(value, expiration.toSeconds());
    }
}
