package com.application.hotelbooking.repositories;

import com.application.hotelbooking.entities.Room;
import com.application.hotelbooking.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findRoomByRoomNumberAndHotelHotelName(@Param("roomNumber") int roomNumber, @Param("hotelName") String hotelName);

    @Query("SELECT r.id FROM Room r JOIN r.hotel h WHERE" +
            "(:singleBeds IS NULL OR r.singleBeds = :singleBeds)" +
            "AND (:doubleBeds IS NULL OR r.doubleBeds = :doubleBeds)" +
            "AND (:roomType IS NULL OR r.roomType = :roomType)" +
            "AND (:hotelName IS NULL OR h.hotelName = :hotelName)" +
            "AND (:city IS NULL OR h.city = :city)"
    )
    List<Long> findRoomsWithConditions(@Param("singleBeds") Integer singleBeds, @Param("doubleBeds") Integer doubleBeds, @Param("roomType") RoomType roomType, @Param("hotelName") String hotelName, @Param("city") String city);
}
