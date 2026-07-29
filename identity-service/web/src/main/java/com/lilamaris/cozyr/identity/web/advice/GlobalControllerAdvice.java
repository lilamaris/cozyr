package com.lilamaris.cozyr.identity.web.advice;

import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.web.config.WebProperties;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.BindException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final WebProperties properties;
    private final HttpStatusResolver httpStatusResolver;
    private final TypeUrlResolver typeUrlResolver;
    private final ProgressCodeResolver progressCodeResolver;

    @ExceptionHandler(ApplicationException.class)
    public ProblemDetail handleApplicationException(ApplicationException exception, HttpServletRequest request) {
        var applicationCode = exception.getApplicationCode();

        var type = applicationCode.type();
        var message = applicationCode.message();
        var code = progressCodeResolver.resolve((IdentityServiceProgressCode) applicationCode);

        var status = httpStatusResolver.resolve(applicationCode.type());
        var typeUri = typeUrlResolver.resolve(applicationCode);

        log.warn(
                "Application error. type={}, path={}, message={}",
                type,
                request.getRequestURI(),
                message
        );

        var problem = ProblemDetail.forStatusAndDetail(status, message);
        problem.setTitle(applicationCode.type().toUpperCase(Locale.ROOT));
        problem.setType(typeUri);
        problem.setProperty("code", code);
        return problem;
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class
    })
    public ProblemDetail handleBadRequest(Exception exception, HttpServletRequest request) {
        log.warn(
                "Bad request. type={}, path={}, message={}",
                exception.getClass().getSimpleName(),
                request.getRequestURI(),
                exception.getMessage()
        );

        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Bad Request");
        problem.setTitle("BAD_REQUEST");
        problem.setType(URI.create(properties.baseUrl()).resolve("errors/").resolve("bad_request"));
        problem.setProperty("code", "BAD_REQUEST");
        return problem;
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ProblemDetail handleAccessDenied(Exception exception, HttpServletRequest request) {
        log.warn(
                "Access denied. type={}, path={}, message={}",
                exception.getClass().getSimpleName(),
                request.getRequestURI(),
                exception.getMessage()
        );

        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access Denied");
        problem.setTitle("FORBIDDEN");
        problem.setType(URI.create(properties.baseUrl()).resolve("errors/").resolve("forbidden"));
        problem.setProperty("code", "FORBIDDEN");
        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(
            IllegalStateException exception,
            HttpServletRequest request
    ) {
        log.error(
                "Illegal state. type={}, path={}, message={}",
                exception.getClass().getSimpleName(),
                request.getRequestURI(),
                exception.getMessage()
        );

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error"
        );
        problem.setTitle("INTERNAL_SERVER_ERROR");
        problem.setType(URI.create(properties.baseUrl()).resolve("errors/").resolve("internal_server_error"));
        problem.setProperty("code", "INTERNAL_SERVER_ERROR");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnhandled(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error(
                "Unhandled exception. type={}, path={}, message={}",
                exception.getClass().getSimpleName(),
                request.getRequestURI(),
                exception.getMessage()
        );

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error"
        );
        problem.setTitle("UNEXPECTED_ERROR");
        problem.setType(URI.create(properties.baseUrl()).resolve("errors/").resolve("unexpected_error"));
        problem.setProperty("code", "UNEXPECTED_ERROR");
        return problem;
    }

    private HttpStatus resolveStatus(Enum<?> code) {
        return switch (code.name()) {
            case "URL_KEY_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
