package com.mforystek.carrentalsystem.service;

import com.mforystek.carrentalsystem.exception.ForbiddenReservationAccessException;
import com.mforystek.carrentalsystem.exception.NoCarsAvailableException;
import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;
import com.mforystek.carrentalsystem.model.Reservation;
import com.mforystek.carrentalsystem.repo.CarRepository;
import com.mforystek.carrentalsystem.repo.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    public ReservationService(ReservationRepository reservationRepository, CarRepository carRepository) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
    }

    public List<Reservation> lookupAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> lookupForCarByPlateNumber(String plateNumber) {
        return reservationRepository.findByRentedCarPlateNumber(plateNumber);
    }

    public List<Reservation> lookupByCarType(CarType carType) {
        return reservationRepository.findByRentedCarCarType(carType);
    }

    public Reservation makeReservation(int customerId, CarType carType, LocalDateTime startDateTime, LocalDateTime endDateTime) throws IllegalArgumentException {
        List<Reservation> overlappingReservations =
                reservationRepository.findByCarTypeForOverlappingReservations(
                        carType, startDateTime, endDateTime);
        List<Car> carsOfGivenType = new ArrayList<>(carRepository.findByCarType(carType));

        overlappingReservations.forEach(reservation -> carsOfGivenType.remove(reservation.getRentedCar()));

        if (carsOfGivenType.isEmpty()) {
            throw new NoCarsAvailableException("No free cars in the selected time range.");
        }

        Reservation reservation = new Reservation(customerId, carsOfGivenType.getFirst(), startDateTime, endDateTime);

        return reservationRepository.save(reservation);
    }

    public void deleteReservation(int customerId, String plateNumber, LocalDateTime startDateTime) throws NoSuchElementException, ForbiddenReservationAccessException {
        Reservation reservationToDelete = reservationRepository.findByRentedCarPlateNumberAndStartDateTime(plateNumber, startDateTime)
                .orElseThrow(() -> new NoSuchElementException("Reservation for car:" + plateNumber + " starting at:" + startDateTime + " does not exist."));

        if (reservationToDelete.getCustomerId() != customerId) {
            throw new ForbiddenReservationAccessException(" Caller customer ID: " + customerId + " does not match reservation customer ID: " + reservationToDelete.getCustomerId());
        }

        reservationRepository.delete(reservationToDelete);
    }
}
