package com.mforystek.carrentalsystem.exception;

public class ForbiddenReservationAccessException extends RuntimeException {
    public ForbiddenReservationAccessException(String message) {
        super(message);
    }
}
