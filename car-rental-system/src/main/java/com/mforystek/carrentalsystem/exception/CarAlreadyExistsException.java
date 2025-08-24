package com.mforystek.carrentalsystem.exception;

public class CarAlreadyExistsException extends RuntimeException {
    public CarAlreadyExistsException(String plateNumber) {
        super("Car with plate number: " + plateNumber + " already exists.");
    }
}
