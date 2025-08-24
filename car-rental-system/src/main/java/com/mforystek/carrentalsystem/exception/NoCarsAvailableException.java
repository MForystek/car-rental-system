package com.mforystek.carrentalsystem.exception;

public class NoCarsAvailableException extends RuntimeException {
    public NoCarsAvailableException(String message) {
        super(message);
    }
}
