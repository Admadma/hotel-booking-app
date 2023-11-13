package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findHotelByHotelName(String hotelName);
}
