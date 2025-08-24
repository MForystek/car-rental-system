package com.mforystek.carrentalsystem.repo;

import com.mforystek.carrentalsystem.model.CarType;
import com.mforystek.carrentalsystem.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    /**
     * @return List of Reservations with matching carType but overlapping with the requested time range from startDateTime to endDateTime.
     */
    @Query("select r from Reservation r where r.rentedCar.carType = ?1 and (?2 <= r.startDateTime and ?3 > r.startDateTime or ?2 < r.endDateTime and ?3 >= r.endDateTime)")
    List<Reservation> findByCarTypeForOverlappingReservations(
            @NonNull CarType carType,
            @NonNull LocalDateTime startDateTime,
            @NonNull LocalDateTime endDateTime
    );

    Optional<Reservation> findByRentedCarPlateNumberAndStartDateTime(String plateNumber, LocalDateTime startDateTime);

    List<Reservation> findByRentedCarPlateNumber(String plateNumber);

    List<Reservation> findByRentedCarCarType(CarType carType);
}
