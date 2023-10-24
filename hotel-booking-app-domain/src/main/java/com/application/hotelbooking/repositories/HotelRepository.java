package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findHotelByHotelName(String hotelName);
}
