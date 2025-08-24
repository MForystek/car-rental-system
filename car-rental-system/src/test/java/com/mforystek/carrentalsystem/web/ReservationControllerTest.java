package com.mforystek.carrentalsystem.web;

import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;
import com.mforystek.carrentalsystem.model.Reservation;
import com.mforystek.carrentalsystem.service.CarService;
import com.mforystek.carrentalsystem.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationControllerTest {
    private static final String PLATE_NUMBER = "KR 12345";
    private static final CarType CAR_TYPE = CarType.Sedan;
    private static final Integer CUSTOMER_ID = 1;
    private static final LocalDateTime START_DATE_TIME = LocalDateTime.of(2025, 8,27, 8, 0);
    private static final LocalDateTime END_DATE_TIME = LocalDateTime.of(2025, 8,28, 8, 0);

    private static final ReservationDTO RESERVATION_DTO = new ReservationDTO(PLATE_NUMBER, START_DATE_TIME, 1);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockitoBean
    private ReservationService reservationServiceMock;

    @MockitoBean
    private CarService carService;

    @Mock
    private Reservation reservationMock;

    @Mock
    private Car carMock;

    @Test
    public void getAllReservationsForCarTest() {
        when(reservationServiceMock.lookupForCarByPlateNumber(PLATE_NUMBER))
                .thenReturn(List.of(reservationMock));
        mockReservation();
        when(carMock.getPlateNumber()).thenReturn(PLATE_NUMBER);

        ResponseEntity<String> response = testRestTemplate.getForEntity("/reservations/{plateNumber}", String.class, PLATE_NUMBER);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(reservationServiceMock).lookupForCarByPlateNumber(PLATE_NUMBER);
    }

    @Test
    public void getAllReservationsForCarTypeTest() {
        when(reservationServiceMock.lookupByCarType(CAR_TYPE))
                .thenReturn(List.of(reservationMock));
        mockReservation();
        when(carMock.getCarType()).thenReturn(CAR_TYPE);
        when(carMock.getPlateNumber()).thenReturn(PLATE_NUMBER);

        ResponseEntity<String> response = testRestTemplate.getForEntity("/reservations/carType?carType={carType}", String.class, CAR_TYPE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(reservationServiceMock).lookupByCarType(CAR_TYPE);
    }

    @Test
    public void makeReservationTest() {
        when(reservationServiceMock.makeReservation(CUSTOMER_ID, CAR_TYPE, START_DATE_TIME, END_DATE_TIME))
                .thenReturn(reservationMock);
        when(carService.lookupByPlateNumber(PLATE_NUMBER)).thenReturn(carMock);
        mockReservation();
        when(carMock.getPlateNumber()).thenReturn(PLATE_NUMBER);
        when(carMock.getCarType()).thenReturn(CAR_TYPE);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/reservations/{customerId}", RESERVATION_DTO, String.class, CUSTOMER_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(reservationServiceMock).makeReservation(CUSTOMER_ID, CAR_TYPE, START_DATE_TIME, END_DATE_TIME);
    }

    @Test
    public void deleteReservationTest() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/reservations/{customerId}",
                HttpMethod.DELETE,
                new HttpEntity<>(RESERVATION_DTO),
                String.class, CUSTOMER_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(reservationServiceMock).deleteReservation(CUSTOMER_ID, PLATE_NUMBER, START_DATE_TIME);
    }

    private void mockReservation() {
        when(reservationMock.getRentedCar()).thenReturn(carMock);
        when(carService.lookupByPlateNumber(PLATE_NUMBER)).thenReturn(carMock);
        when(reservationMock.getStartDateTime()).thenReturn(START_DATE_TIME);
        when(reservationMock.getEndDateTime()).thenReturn(END_DATE_TIME);
    }
}
