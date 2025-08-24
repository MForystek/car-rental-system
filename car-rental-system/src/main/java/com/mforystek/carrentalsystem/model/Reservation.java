package com.mforystek.carrentalsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer customerId;

    @ManyToOne
    @JoinColumn(name = "car_plateNumber", nullable = false)
    @JsonBackReference("car-reservations")
    private Car rentedCar;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startDateTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endDateTime;

    protected Reservation() {
    }

    public Reservation(Integer customerId, Car rentedCar, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.customerId = customerId;
        this.rentedCar = rentedCar;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Car getRentedCar() {
        return rentedCar;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", rentedCar=" + rentedCar +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
