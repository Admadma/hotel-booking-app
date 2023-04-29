package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
//    @Query(
//        value = "SELECT * FROM reservations r WHERE r.room_id = ?1",
//        nativeQuery = true)
//    List<Reservation> getReservationsOfRoom(Long room_id);

    List<Reservation> findAllByRoom(Room room);
    List<Reservation> findAllByUser(User user);
}
