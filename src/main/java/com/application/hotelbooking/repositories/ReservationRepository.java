package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findReservationById(Long id);

//    @Query("SELECT * FROM reservations WHERE room_id=")
//    List<Reservation> getReservationsOfRoom(Long room);
}
