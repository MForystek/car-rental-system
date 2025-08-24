package com.mforystek.carrentalsystem.service;

import com.mforystek.carrentalsystem.exception.ForbiddenReservationAccessException;
import com.mforystek.carrentalsystem.exception.NoCarsAvailableException;
import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;
import com.mforystek.carrentalsystem.model.Reservation;
import com.mforystek.carrentalsystem.repo.CarRepository;
import com.mforystek.carrentalsystem.repo.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    private static final String PLATE_NUMBER = "KR 12345";
    private static final CarType CAR_TYPE = CarType.Sedan;
    private static final Integer RESERVATION_ID = 1;
    private static final Integer CUSTOMER_ID = 1;
    private static final Integer CUSTOMER_ID2 = 2;
    private static final LocalDateTime START_DATE_TIME = LocalDateTime.of(2025, 8,27, 8, 0);
    private static final LocalDateTime END_DATE_TIME = LocalDateTime.of(2025, 8,28, 8, 0);


    @Mock
    private Car carMock;

    @Mock
    private Reservation reservationMock;

    @Mock
    private CarRepository carRepositoryMock;

    @Mock
    private ReservationRepository reservationRepositoryMock;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void lookupForCarByPlateNumberTest() {
        when(reservationRepositoryMock.findByRentedCarPlateNumber(PLATE_NUMBER))
                .thenReturn(List.of(reservationMock));
        when(reservationMock.getId())
                .thenReturn(RESERVATION_ID);

        List<Reservation> reservations = reservationService.lookupForCarByPlateNumber(PLATE_NUMBER);

        assertThat(reservations.size()).isEqualTo(1);
        assertThat(reservations.getFirst().getId()).isEqualTo(RESERVATION_ID);
    }

    @Test
    public void lookupByCarTypeTest() {
        when(reservationRepositoryMock.findByRentedCarCarType(CAR_TYPE))
                .thenReturn(List.of(reservationMock));
        when(reservationMock.getRentedCar())
                .thenReturn(carMock);
        when(carMock.getCarType())
                .thenReturn(CAR_TYPE);

        List<Reservation> reservations = reservationService.lookupByCarType(CAR_TYPE);

        assertThat(reservations.size())
                .isEqualTo(1);
        assertThat(reservations.getFirst().getRentedCar().getCarType())
                .isEqualTo(CAR_TYPE);
    }

    @Test
    public void makeReservationWhenNoOverlapsTest() {
        when(reservationRepositoryMock.findByCarTypeForOverlappingReservations(CAR_TYPE, START_DATE_TIME, END_DATE_TIME))
                .thenReturn(List.of());
        when(reservationRepositoryMock.save(any())).thenReturn(reservationMock);
        when(carRepositoryMock.findByCarType(CAR_TYPE)).thenReturn(List.of(carMock));

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);

        reservationService.makeReservation(CUSTOMER_ID, CAR_TYPE, START_DATE_TIME, END_DATE_TIME);

        verify(reservationRepositoryMock).save(reservationCaptor.capture());

        Reservation captured = reservationCaptor.getValue();
        assertThat(captured.getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(captured.getStartDateTime()).isEqualTo(START_DATE_TIME);
        assertThat(captured.getEndDateTime()).isEqualTo(END_DATE_TIME);
    }

    @Test
    public void makeReservationWhenOverlapsTest() {
        when(reservationRepositoryMock.findByCarTypeForOverlappingReservations(CAR_TYPE, START_DATE_TIME, END_DATE_TIME))
                .thenReturn(List.of(reservationMock));
        when(carRepositoryMock.findByCarType(CAR_TYPE)).thenReturn(List.of(carMock));
        when(reservationMock.getRentedCar()).thenReturn(carMock);

        assertThatThrownBy(() -> reservationService.makeReservation(CUSTOMER_ID, CAR_TYPE, START_DATE_TIME, END_DATE_TIME))
                .isInstanceOf(NoCarsAvailableException.class);
    }

    @Test
    public void deleteReservationTest() {
        when(reservationRepositoryMock.findByRentedCarPlateNumberAndStartDateTime(PLATE_NUMBER, START_DATE_TIME))
                .thenReturn(Optional.of(reservationMock));
        when(reservationMock.getCustomerId()).thenReturn(CUSTOMER_ID);

        reservationService.deleteReservation(CUSTOMER_ID, PLATE_NUMBER, START_DATE_TIME);

        verify(reservationRepositoryMock).delete(reservationMock);
    }

    @Test
    public void deleteNonExistingReservationTest() {
        when(reservationRepositoryMock.findByRentedCarPlateNumberAndStartDateTime(PLATE_NUMBER, START_DATE_TIME))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.deleteReservation(CUSTOMER_ID, PLATE_NUMBER, START_DATE_TIME))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void deleteReservationWithWrongCustomerIdTest() {
        when(reservationRepositoryMock.findByRentedCarPlateNumberAndStartDateTime(PLATE_NUMBER, START_DATE_TIME))
                .thenReturn(Optional.of(reservationMock));
        when(reservationMock.getCustomerId()).thenReturn(CUSTOMER_ID2);

        assertThatThrownBy(() -> reservationService.deleteReservation(CUSTOMER_ID, PLATE_NUMBER, START_DATE_TIME))
                .isInstanceOf(ForbiddenReservationAccessException.class);
    }
}
