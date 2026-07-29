package com.lilamaris.cozyr.identity.web.advice;

import com.lilamaris.cozyr.identity.web.config.WebProperties;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TypeUrlResolver {
    private final WebProperties properties;

    public URI resolve(ApplicationCode applicationCode) {
        var type = applicationCode.type();
        return URI.create(properties.baseUrl()).resolve("errors/").resolve(type.toLowerCase(Locale.ROOT));
    }
}
