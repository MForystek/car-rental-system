package com.mforystek.carrentalsystem.repo;

import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;
import com.mforystek.carrentalsystem.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationRepositoryTest {
    private static final String PLATE_NUMBER = "KR 12345";
    private static final CarType CAR_TYPE = CarType.Sedan;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    private List<Car> initialCars = List.of(
            new Car(PLATE_NUMBER, CAR_TYPE, null),
            new Car("KRK 1234", CarType.SUV, null),
            new Car("RJS 1234", CarType.van, null)
    );

    private List<Reservation> initialReservations = List.of(
            new Reservation(1, initialCars.get(0),
                    LocalDateTime.of(2025, Month.AUGUST, 27, 8, 0),
                    LocalDateTime.of(2025, Month.AUGUST, 28, 8, 0)),
            new Reservation(2, initialCars.get(0),
                    LocalDateTime.of(2025, Month.AUGUST, 28, 8, 0),
                    LocalDateTime.of(2025, Month.AUGUST, 29, 8, 0)),
            new Reservation(1, initialCars.get(1),
                    LocalDateTime.of(2025, Month.AUGUST, 27, 15, 15),
                    LocalDateTime.of(2025, Month.SEPTEMBER, 15, 15, 15)),
            new Reservation(3, initialCars.get(2),
                    LocalDateTime.of(2025, Month.JULY, 12, 18, 4),
                    LocalDateTime.of(2025, Month.NOVEMBER, 30, 18, 4))
    );

    @BeforeEach
    public void insertCarsThenReservations() {
        initialCars.forEach(car -> carRepository.save(car));

        initialReservations.forEach(reservations -> reservationRepository.save(reservations));
    }

    @Test
    public void findByRentedCarPlateNumberTest() {
        List<Reservation> reservations = reservationRepository.findByRentedCarPlateNumber(PLATE_NUMBER);

        assertThat(reservations.size())
                .isEqualTo(2);
        assertThat(reservations.getFirst().getRentedCar().getPlateNumber())
                .isEqualTo(PLATE_NUMBER);
    }

    @Test
    public void findByRentedCarCarTypeTest() {
        List<Reservation> reservations = reservationRepository.findByRentedCarCarType(CAR_TYPE);

        assertThat(reservations.size())
                .isEqualTo(2);
        assertThat(reservations.getFirst().getRentedCar().getCarType())
                .isEqualTo(CAR_TYPE);
    }

    @Test
    public void findNoOverlappingReservationsTest() {
        LocalDateTime startDateTime = LocalDateTime.of(2025, 12, 12, 12, 12);
        LocalDateTime endDateTime = LocalDateTime.of(2026, 12, 12, 12, 12);

        List<Reservation> reservations = reservationRepository
                .findByCarTypeForOverlappingReservations(CAR_TYPE, startDateTime, endDateTime);

        assertThat(reservations.isEmpty()).isTrue();
    }

    @Test
    public void findOverlappingReservationsTest() {
        LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2026, 1, 1, 0, 0);

        List<Reservation> reservations = reservationRepository
                .findByCarTypeForOverlappingReservations(CAR_TYPE, startDateTime, endDateTime);

        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations.getLast().getId()).isEqualTo(initialReservations.get(1).getId());
    }
}
