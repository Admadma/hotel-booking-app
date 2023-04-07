package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findReservationById(Long id);

//    @Query("SELECT * FROM reservations WHERE room_id=?1")
//    @Query("SELECT * FROM reservations;")
    @Query(
        value = "SELECT * FROM reservations r WHERE r.room_id = ?1 AND r.start_date > ?2",
        nativeQuery = true)
    List<Reservation> getReservationsOfRoom(Long room_id, LocalDate endDate);
}
