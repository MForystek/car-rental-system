package com.mforystek.carrentalsystem.web;

import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carService.lookupAll();
    }

    @GetMapping("/{plateNumber}")
    public Car getCarByPlateNumber(@PathVariable("plateNumber") String plateNumber) {
        return carService.lookupByPlateNumber(plateNumber);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Car addNewCar(@RequestBody Car carToAdd) {
        return carService.addCar(carToAdd);
    }

    @DeleteMapping("/{plateNumber}")
    public void deleteCarByPlateNumber(@PathVariable("plateNumber") String plateNumber) {
        carService.deleteCarByPlate(plateNumber);
    }
}
