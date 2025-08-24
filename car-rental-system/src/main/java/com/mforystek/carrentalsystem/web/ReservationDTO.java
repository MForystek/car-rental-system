package com.mforystek.carrentalsystem.web;

import com.mforystek.carrentalsystem.model.Reservation;

import java.time.Duration;
import java.time.LocalDateTime;

public class ReservationDTO {
    private static final int SECONDS_IN_DAY = 86_400;

    private String plateNumber;

    private LocalDateTime startDateTime;

    private Integer durationInDays;

    public ReservationDTO() {
    }

    public ReservationDTO(String plateNumber, LocalDateTime startDateTime, Integer durationInDays) {
        this.plateNumber = plateNumber;
        this.startDateTime = startDateTime;
        this.durationInDays = Math.max(0, durationInDays);
    }

    public ReservationDTO(Reservation reservation) {
        this.plateNumber = reservation.getRentedCar().getPlateNumber();
        this.startDateTime = reservation.getStartDateTime();
        this.durationInDays = convertDateTimesToDifferenceInDays(reservation.getStartDateTime(), reservation.getEndDateTime());
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Integer getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Integer durationInDays) {
        this.durationInDays = Math.max(0, durationInDays);
    }


    private int convertDateTimesToDifferenceInDays(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("End date time cannot be before start date time");
        }
        long difference = Duration.between(startDateTime, endDateTime).toSeconds();
        return (int) Math.ceil((double) difference / SECONDS_IN_DAY);
    }
}
