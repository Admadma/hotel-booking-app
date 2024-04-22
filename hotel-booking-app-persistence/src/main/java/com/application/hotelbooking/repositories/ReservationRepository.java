package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Reservation;
import com.application.hotelbooking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByRoomId(Long id);
    List<Reservation> findAllByUserId(Long id);
    Optional<Reservation> findByUuid(UUID uuid);
}
