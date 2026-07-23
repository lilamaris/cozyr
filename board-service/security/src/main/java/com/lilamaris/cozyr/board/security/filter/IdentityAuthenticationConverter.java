package com.lilamaris.cozyr.board.security.filter;

import com.lilamaris.cozyr.identity.contract.schema.Identity;
import com.lilamaris.cozyr.identity.contract.schema.SimpleIdentity;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.jspecify.annotations.NullMarked;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@NullMarked
public class IdentityAuthenticationConverter implements Converter<Jwt, IdentityAuthenticationToken> {
    private static final String DISPLAY_NAME_CLAIM = "displayName";

    @Override
    public IdentityAuthenticationToken convert(Jwt source) {
        ObjectPrecondition.requireNonNull(source, "source");

        var identity = extractIdentity(source);

        return new IdentityAuthenticationToken(identity, source);
    }

    private Identity extractIdentity(Jwt source) {
        var subject = ObjectPrecondition.requireNonNull(source.getSubject(), "subject");
        var id = UUID.fromString(subject);
        var displayName = StringPrecondition.requireNonBlank(source.getClaim(DISPLAY_NAME_CLAIM), "displayName");

        return SimpleIdentity.of(id, displayName);
    }
}
