package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findReservationById(Long id);

    @Query(
        value = "SELECT * FROM reservations r WHERE r.room_id = ?1",
        nativeQuery = true)
    List<Reservation> getReservationsOfRoom(Long room_id);
}
