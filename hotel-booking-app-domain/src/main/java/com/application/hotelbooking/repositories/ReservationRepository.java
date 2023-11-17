package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByRoomId(Long id);
    List<Reservation> findAllByUserId(Long id);
}
