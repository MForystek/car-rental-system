package com.mforystek.carrentalsystem.dto;

import com.mforystek.carrentalsystem.model.Reservation;

import java.time.Duration;
import java.time.LocalDateTime;

public record ReservationDTO(String plateNumber, LocalDateTime startDateTime, Integer durationInDays) {
    private static final int SECONDS_IN_DAY = 86_400;

    public ReservationDTO(String plateNumber, LocalDateTime startDateTime, Integer durationInDays) {
        this.plateNumber = plateNumber;
        this.startDateTime = startDateTime;
        this.durationInDays = Math.max(0, durationInDays);
    }

    public ReservationDTO(Reservation reservation) {
        this(reservation.getRentedCar().getPlateNumber(),
                reservation.getStartDateTime(),
                calculateDurationInDays(reservation.getStartDateTime(), reservation.getEndDateTime()));
    }

    private static int calculateDurationInDays(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("End date time cannot be before start date time");
        }
        long difference = Duration.between(startDateTime, endDateTime).toSeconds();
        return (int) Math.ceil((double) difference / SECONDS_IN_DAY);
    }
}
