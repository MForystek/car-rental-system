package com.mforystek.carrentalsystem.repo;

import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class CarRepositoryTest {
    private static final String PLATE_NUMBER = "KR 12345";
    private static final CarType CAR_TYPE = CarType.Sedan;
    private static final CarType CAR_TYPE2 = CarType.SUV;

    @Autowired
    private CarRepository carRepository;

    private List<Car> cars = List.of(
            new Car(PLATE_NUMBER, CAR_TYPE, null),
            new Car("KRK 1234", CAR_TYPE2, null)
    );

    @BeforeEach
    public void loadCarsToDB() {
        cars.forEach(car -> carRepository.save(car));
    }

    @Test
    public void addCarTest() {
        loadCarsToDB();

        assertThat(carRepository.count())
                .isEqualTo(cars.size());
        assertThat(carRepository.findByPlateNumber(PLATE_NUMBER).get().getCarType())
                .isEqualTo(CAR_TYPE);
        assertThat(carRepository.findByCarType(CAR_TYPE).getFirst().getPlateNumber())
                .isEqualTo(PLATE_NUMBER);
    }

    @Test
    public void deleteCarTest() {
        loadCarsToDB();

        carRepository.deleteById(PLATE_NUMBER);

        assertThat(carRepository.count())
                .isEqualTo(cars.size() - 1);
        assertThat(carRepository.findById(PLATE_NUMBER))
                .isEmpty();
    }
}
