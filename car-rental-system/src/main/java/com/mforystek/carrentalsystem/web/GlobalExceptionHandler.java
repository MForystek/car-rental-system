package com.mforystek.carrentalsystem.web;

import com.mforystek.carrentalsystem.exception.CarAlreadyExistsException;
import com.mforystek.carrentalsystem.exception.ForbiddenReservationAccessException;
import com.mforystek.carrentalsystem.exception.NoCarsAvailableException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CarAlreadyExistsException.class)
    public final ResponseEntity<Object> handleCarAlreadyExistsException(CarAlreadyExistsException e, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        return createResponseEntity(pd, HttpHeaders.EMPTY, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        return createResponseEntity(pd, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleForbiddenReservationAccessException(ForbiddenReservationAccessException e, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        return createResponseEntity(pd, HttpHeaders.EMPTY, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleNoCarsAvailableException(NoCarsAvailableException e, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        return createResponseEntity(pd, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, request);
    }
}
