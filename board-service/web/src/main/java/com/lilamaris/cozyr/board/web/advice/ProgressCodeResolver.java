package com.lilamaris.cozyr.board.web.advice;

import com.lilamaris.shrturl.kernel.application.exception.ApplicationProgressCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressCodeResolver {
    private final static String SEPARATOR = ".";

    public String resolve(ApplicationProgressCode applicationProgressCode) {
        var resourceName = applicationProgressCode.resourceName();
        var type = applicationProgressCode.type();
        var reason = applicationProgressCode.reason().getCanonicalName();

        return resourceName + SEPARATOR + reason + SEPARATOR + type;
    }
}
