package com.lilamaris.cozyr.board.security.filter;

import com.lilamaris.cozyr.identity.contract.schema.Identity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

public class IdentityAuthenticationToken extends AbstractAuthenticationToken {
    private final Identity identity;
    private final Jwt jwt;

    public IdentityAuthenticationToken(
            Identity identity,
            Jwt jwt
    ) {
        super(Set.of());
        this.identity = identity;
        this.jwt = jwt;
        setAuthenticated(true);
    }

    @Override
    public @Nullable Object getPrincipal() {
        return identity;
    }

    @Override
    public @Nullable Object getCredentials() {
        return jwt;
    }

    @Override
    public @NullMarked String getName() {
        return identity.displayName();
    }
}
