package com.lilamaris.cozyr.reservation.web.advice;

import com.lilamaris.cozyr.kernel.web.response.ProblemDetailFactory;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.BindException;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(ApplicationException.class)
    public ProblemDetail handleApplicationException(ApplicationException exception, HttpServletRequest request) {
        var applicationCode = exception.getApplicationCode();

        log.warn(
                "Application error. type={}, path={}, message={}",
                applicationCode.type(),
                request.getRequestURI(),
                applicationCode.message()
        );

        return problemDetailFactory.from(applicationCode);
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

        return problemDetailFactory.badRequest();
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ProblemDetail handleAccessDenied(Exception exception, HttpServletRequest request) {
        log.warn(
                "Access denied. type={}, path={}, message={}",
                exception.getClass().getSimpleName(),
                request.getRequestURI(),
                exception.getMessage()
        );

        return problemDetailFactory.accessDenied();
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

        return problemDetailFactory.internalServerError();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFound(
            NoResourceFoundException exception,
            HttpServletRequest request
    ) {
        log.error(
                "No resource found. type={}, path={}, message={}",
                exception.getClass().getSimpleName(),
                request.getRequestURI(),
                exception.getMessage()
        );

        return problemDetailFactory.notFound();
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
                exception.getMessage(),
                exception
        );

        return problemDetailFactory.unexpectedError();
    }
}
