package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.config.ApplicationProperties;
import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.generator.RefreshTokenGenerator;
import com.lilamaris.cozyr.identity.application.model.AuthenticatedPrincipal;
import com.lilamaris.cozyr.identity.application.port.in.IssueTokenUseCase;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticatedResult;
import com.lilamaris.cozyr.identity.application.port.in.result.TokenResult;
import com.lilamaris.cozyr.identity.application.port.out.PrincipalReader;
import com.lilamaris.cozyr.identity.contract.codec.ScopeCodec;
import com.lilamaris.cozyr.identity.contract.schema.Scope;
import com.lilamaris.cozyr.identity.domain.UserScope;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IssueTokenService implements IssueTokenUseCase {
    private final PrincipalReader principalReader;
    private final ApplicationProperties properties;
    private final ScopeCodec scopeCodec;
    private final JwtEncoder jwtEncoder;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final Clock clock;

    public IssueTokenService(
            PrincipalReader principalReader,
            ApplicationProperties properties,
            ScopeCodec scopeCodec,
            JwtEncoder jwtEncoder,
            RefreshTokenGenerator refreshTokenGenerator,
            Clock clock
    ) {
        this.principalReader = principalReader;
        this.properties = properties;
        this.scopeCodec = scopeCodec;
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.clock = clock;
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResult issue(AuthenticatedResult result) {
        var userId = result.userId();

        var principal = principalReader.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(IdentityServiceProgressCode.USER_NOT_FOUND));

        var displayName = principal.displayName();
        var scopes = principal.scopes();
        var access = buildAccess(userId, displayName, scopes);
        var refresh = refreshTokenGenerator.generate();

        return TokenResult.of(access, refresh);
    }

    private String buildAccess(UUID userId, String displayName, Set<Scope> scopes) {
        var now = clock.instant();
        var expiresAt = now.plus(properties.expiration());
        var encoded = scopeCodec.encode(scopes);

        var claims = JwtClaimsSet.builder()
                .issuer(properties.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userId.toString())
                .claim("scopes", encoded)
                .claim("displayName", displayName)
                .build();
        var parameters = JwtEncoderParameters.from(claims);
        return jwtEncoder.encode(parameters).getTokenValue();
    }
}
