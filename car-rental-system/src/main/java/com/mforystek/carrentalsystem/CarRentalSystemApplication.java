package com.mforystek.carrentalsystem;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarRentalSystemApplication {
	@Bean
	public OpenAPI swaggerHeader() {
		return new OpenAPI().info((new Info())
				.description("API for cars and reservations.")
				.title("Car Rental System API")
				.version("0.0.1"));
	}

	public static void main(String[] args) {
		SpringApplication.run(CarRentalSystemApplication.class, args);
	}

}
