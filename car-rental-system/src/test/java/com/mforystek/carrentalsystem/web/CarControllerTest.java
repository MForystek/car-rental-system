package com.mforystek.carrentalsystem.web;

import com.mforystek.carrentalsystem.model.Car;
import com.mforystek.carrentalsystem.model.CarType;
import com.mforystek.carrentalsystem.service.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class CarControllerTest {
    private static final String PLATE_NUMBER = "KR 12345";
    private static final CarType CAR_TYPE = CarType.Sedan;
    private static final CarType CAR_TYPE2 = CarType.SUV;
    private static final Car CAR = new Car(PLATE_NUMBER, CAR_TYPE, List.of());
    private static final Car CAR2 = new Car(PLATE_NUMBER, CAR_TYPE2, List.of());

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockitoBean
    private CarService carServiceMock;

    @Test
    public void getAllTest() {
        ResponseEntity<List> response = testRestTemplate.getForEntity("/cars", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(carServiceMock).lookupAll();
    }

    @Test
    public void getCarByPlateNumberTest() {
        when(carServiceMock.lookupByPlateNumber(any())).thenReturn(CAR);

        ResponseEntity<Car> response = testRestTemplate.getForEntity("/cars/{plateNumber}", Car.class, PLATE_NUMBER);
        Car responseCar = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(carServiceMock).lookupByPlateNumber(PLATE_NUMBER);
        assertThat(responseCar.getPlateNumber()).isEqualTo(PLATE_NUMBER);
    }

    @Test
    public void addNewCarTest() {
        when(carServiceMock.addCar(any())).thenReturn(CAR);

        ResponseEntity<Car> response = testRestTemplate.postForEntity("/cars", CAR, Car.class);
        Car responseCar = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(carServiceMock).addCar(CAR);
        assertThat(responseCar.getPlateNumber()).isEqualTo(PLATE_NUMBER);
    }

    @Test
    public void deleteCarByPlateNumberTest() {
        testRestTemplate.delete("/cars/{plateNumber}", PLATE_NUMBER);

        verify(carServiceMock).deleteCarByPlate(PLATE_NUMBER);
    }
}