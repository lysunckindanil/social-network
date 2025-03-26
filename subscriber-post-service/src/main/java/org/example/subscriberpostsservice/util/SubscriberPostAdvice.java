package org.example.subscriberpostsservice.util;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class SubscriberPostAdvice {

    @ExceptionHandler({BindException.class})
    public ResponseEntity<?> handleBindException(BindException e) {
        return handleBadRequestException(new BadRequestException(Objects.requireNonNull(e.getFieldError())
                .getDefaultMessage()));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        return handleBadRequestException(new BadRequestException(e.getConstraintViolations().stream().findAny().get().getMessage()));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, BadRequestException.class.getName());
        problemDetail.setProperty("error", e.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler({HttpMessageConversionException.class})
    public ResponseEntity<?> handleHttpMessageConversionException() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, HttpMessageConversionException.class.getName());
        problemDetail.setProperty("error", "JSON parsing error");
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
