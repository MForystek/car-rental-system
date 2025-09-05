package com.mforystek.carrentalsystem.web;

import com.mforystek.carrentalsystem.dto.ReservationDTO;
import com.mforystek.carrentalsystem.model.CarType;
import com.mforystek.carrentalsystem.model.Reservation;
import com.mforystek.carrentalsystem.service.CarService;
import com.mforystek.carrentalsystem.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;
    private CarService carService;

    public ReservationController(ReservationService reservationService, CarService carService) {
        this.reservationService = reservationService;
        this.carService = carService;
    }

    @GetMapping
    @Operation(summary = "Get all reservations")
    public List<ReservationDTO> getAllReservations() {
        return reservationService.lookupAll().stream().map(ReservationDTO::new).toList();
    }

    @GetMapping("/{plateNumber}")
    @Operation(summary = "Get reservations for a car with given plate number")
    public List<ReservationDTO> getAllReservationsForCar(@PathVariable("plateNumber") String plateNumber) {
        return reservationService.lookupForCarByPlateNumber(plateNumber).stream().map(ReservationDTO::new).toList();
    }

    @GetMapping("/carType")
    @Operation(summary = "Get reservations for all cars with given car type")
    public List<ReservationDTO> getAllReservationsForCarType(@PathParam("carType") CarType carType) {
        return reservationService.lookupByCarType(carType).stream().map(ReservationDTO::new).toList();
    }

    @PostMapping("/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Make a new reservation for given customer ID")
    public Reservation makeReservation(@PathVariable("customerId") Integer customerId, @RequestBody ReservationDTO reservationDTO) {
        CarType carType = carService.lookupByPlateNumber(reservationDTO.plateNumber()).getCarType();
        LocalDateTime endDateTime = reservationDTO.startDateTime().plusDays(reservationDTO.durationInDays());
        return reservationService.makeReservation(customerId, carType, reservationDTO.startDateTime(), endDateTime);
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete reservation for given customer ID")
    public void deleteReservation(@PathVariable("customerId") Integer customerId, @RequestBody ReservationDTO reservationDTO) {
        reservationService.deleteReservation(customerId, reservationDTO.plateNumber(), reservationDTO.startDateTime());
    }
}
