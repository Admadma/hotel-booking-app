package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findReservationById(Long id);
}
