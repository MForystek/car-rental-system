package com.mforystek.carrentalsystem.dto;

import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;

import java.util.List;

public record CarDTO(
        String plateNumber,
        CarType carType,
        List<ReservationDTO> reservations) {
    public CarDTO(Car car) {
        this(car.getPlateNumber(), car.getCarType(), car.getReservations().stream().map(ReservationDTO::new).toList());
    }
}
