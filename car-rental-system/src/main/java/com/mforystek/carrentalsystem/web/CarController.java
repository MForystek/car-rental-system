package com.mforystek.carrentalsystem.web;

import com.mforystek.carrentalsystem.dto.CarDTO;
import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDTO> getAllCars() {
        return carService.lookupAll().stream().map(CarDTO::new).toList();
    }

    @GetMapping("/{plateNumber}")
    public CarDTO getCarByPlateNumber(@PathVariable("plateNumber") String plateNumber) {
        return new CarDTO(carService.lookupByPlateNumber(plateNumber));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CarDTO addNewCar(@RequestBody Car carToAdd) {
        return new CarDTO(carService.addCar(carToAdd));
    }

    @DeleteMapping("/{plateNumber}")
    public void deleteCarByPlateNumber(@PathVariable("plateNumber") String plateNumber) {
        carService.deleteCarByPlate(plateNumber);
    }
}
