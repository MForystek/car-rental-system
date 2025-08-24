package com.mforystek.carrentalsystem.service;

import com.mforystek.carrentalsystem.exception.CarAlreadyExistsException;
import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;
import com.mforystek.carrentalsystem.model.Reservation;
import com.mforystek.carrentalsystem.repo.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    private static final String PLATE_NUMBER = "KR 12345";
    private static final CarType CAR_TYPE = CarType.Sedan;
    private static final List<Reservation> RESERVATIONS = List.of();
    private static Car CAR;

    @Mock
    private CarRepository carRepositoryMock;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void instantiateCar() {
        CAR = new Car(PLATE_NUMBER, CAR_TYPE, RESERVATIONS);
    }

    @Test
    public void lookupByPlateNumberTest() {
        when(carRepositoryMock.findByPlateNumber(PLATE_NUMBER)).thenReturn(Optional.of(CAR));

        Car car = carService.lookupByPlateNumber(PLATE_NUMBER);

        assertCarMatches(car, PLATE_NUMBER, CAR_TYPE, RESERVATIONS);
    }

    @Test
    public void addingCarTest() {
        when(carRepositoryMock.findByPlateNumber(PLATE_NUMBER)).thenReturn(Optional.empty());
        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        carService.addCar(CAR);

        verify(carRepositoryMock).save(carCaptor.capture());

        Car captured = carCaptor.getValue();
        assertCarMatches(captured, PLATE_NUMBER, CAR_TYPE, RESERVATIONS);
    }

    @Test
    public void addingCarWithSamePlateNumber() {
        when(carRepositoryMock.findByPlateNumber(PLATE_NUMBER)).thenReturn(Optional.of(CAR));

        assertThatThrownBy(() -> carService.addCar(CAR))
                .isInstanceOf(CarAlreadyExistsException.class);
    }

    @Test
    public void deletingCarTest() {
        when(carRepositoryMock.findByPlateNumber(PLATE_NUMBER)).thenReturn(Optional.of(CAR));
        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        carService.deleteCarByPlate(carService.lookupByPlateNumber(PLATE_NUMBER).getPlateNumber());

        verify(carRepositoryMock).delete(carCaptor.capture());

        Car captured = carCaptor.getValue();
        assertCarMatches(captured, PLATE_NUMBER, CAR_TYPE, RESERVATIONS);
    }

    @Test
    public void deletingNonExistingCarTest() {
        when(carRepositoryMock.findByPlateNumber(PLATE_NUMBER)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.deleteCarByPlate(PLATE_NUMBER))
                .isInstanceOf(NoSuchElementException.class);
    }

    private void assertCarMatches(Car car, String plateNumber, CarType carType, List<Reservation> reservations) {
        assertThat(car.getPlateNumber()).isEqualTo(plateNumber);
        assertThat(car.getCarType()).isEqualTo(carType);
        assertThat(car.getReservations()).isEqualTo(reservations);
    }
}
