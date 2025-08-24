package com.mforystek.carrentalsystem.repo;

import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, String> {
    Optional<Car> findByPlateNumber(String plateNumber);

    List<Car> findByCarType(CarType carType);
}
