package com.lilamaris.cozyr.kernel.web.response;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationCode;
import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;

import java.util.Locale;

@RequiredArgsConstructor
public class ProblemDetailFactory {
    private final ErrorCodeResolver errorCodeResolver;
    private final ErrorStatusResolver errorStatusResolver;
    private final ErrorTypeUriResolver errorTypeUriResolver;

    public ProblemDetail from(ApplicationCode applicationCode) {
        return from(DefaultErrorDescriptor.from(applicationCode));
    }

    public ProblemDetail from(ProcessReason reason, String type, String message) {
        return from(new DefaultErrorDescriptor(reason, type, message));
    }

    public ProblemDetail from(ProcessReason reason, String resourceName, String type, String message) {
        return from(new DefaultErrorDescriptor(reason, resourceName, type, message));
    }

    public ProblemDetail from(ErrorDescriptor errorDescriptor) {
        ObjectPrecondition.requireNonNull(errorDescriptor, "errorDescriptor");

        var type = errorDescriptor.type();
        var message = errorDescriptor.message();

        var status = errorStatusResolver.resolve(type);
        var typeUri = errorTypeUriResolver.resolve(type);
        var errorCode = errorCodeResolver.resolve(errorDescriptor);

        var title = type.toUpperCase(Locale.ROOT);

        var problem = ProblemDetail.forStatusAndDetail(status, message);

        problem.setTitle(title);
        problem.setType(typeUri);
        problem.setProperty("code", errorCode);

        return problem;
    }

    public ProblemDetail badRequest() {
        return from(StandardErrorDescriptor.BAD_REQUEST);
    }

    public ProblemDetail notFound() {
        return from(StandardErrorDescriptor.NOT_FOUND);
    }

    public ProblemDetail accessDenied() {
        return from(StandardErrorDescriptor.ACCESS_DENIED);
    }

    public ProblemDetail unauthorized() {
        return from(StandardErrorDescriptor.UNAUTHORIZED);
    }

    public ProblemDetail internalServerError() {
        return from(StandardErrorDescriptor.INTERNAL_SERVER_ERROR);
    }

    public ProblemDetail unexpectedError() {
        return from(StandardErrorDescriptor.UNEXPECTED_ERROR);
    }
}
