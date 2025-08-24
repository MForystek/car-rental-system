package com.mforystek.carrentalsystem.service;

import com.mforystek.carrentalsystem.exception.CarAlreadyExistsException;
import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.repo.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    private CarRepository  carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> lookupAll() {
        return carRepository.findAll();
    }

    public Car lookupByPlateNumber(String plateNumber) throws NoSuchElementException {
        return carRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new NoSuchElementException("No car with plate number: " + plateNumber));
    }

    public Car addCar(Car carToAdd) throws CarAlreadyExistsException {
        Optional<Car> existingCar = carRepository.findByPlateNumber(carToAdd.getPlateNumber());

        existingCar.ifPresent(car -> {
            throw new CarAlreadyExistsException(car.getPlateNumber());
        });

        return carRepository.save(carToAdd);
    }

    public void deleteCarByPlate(String plateNumber) throws NoSuchElementException {
        Car carToDelete = carRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new NoSuchElementException("No car with plate number: " + plateNumber));

        carRepository.delete(carToDelete);
    }
}
