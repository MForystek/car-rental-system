package com.mforystek.carrentalsystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Car {
    @Id
    private String plateNumber;

    @Column
    private CarType carType;

    @OneToMany(mappedBy = "rentedCar", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("car-reservations")
    private List<Reservation> reservations;

    protected Car() {
    }

    public Car(String plateNumber, CarType carType, List<Reservation> reservations) {
        this.plateNumber = plateNumber;
        this.carType = carType;
        this.reservations = reservations;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public CarType getCarType() {
        return carType;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    @Override
    public String toString() {
        return "Car{" +
                "plateNumber='" + plateNumber + "' "+
                ", carType=" + carType +
                ", reservations=" + reservations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(plateNumber, car.plateNumber) && carType == car.carType && Objects.equals(reservations, car.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateNumber, carType, reservations);
    }
}
