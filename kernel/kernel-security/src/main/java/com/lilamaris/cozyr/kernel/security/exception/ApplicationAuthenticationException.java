package com.lilamaris.cozyr.kernel.security.exception;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationCode;
import org.springframework.security.core.AuthenticationException;

public class ApplicationAuthenticationException extends AuthenticationException {
    private final ApplicationCode applicationCode;

    public ApplicationAuthenticationException(ApplicationCode applicationCode) {
        super(ObjectPrecondition.requireNonNull(applicationCode, "applicationCode").message());
        this.applicationCode = applicationCode;
    }

    public ApplicationAuthenticationException(ApplicationCode applicationCode, Throwable cause) {
        super(ObjectPrecondition.requireNonNull(applicationCode, "applicationCode").message(), cause);
        this.applicationCode = applicationCode;
    }

    public ApplicationCode getApplicationCode() {
        return applicationCode;
    }
}
