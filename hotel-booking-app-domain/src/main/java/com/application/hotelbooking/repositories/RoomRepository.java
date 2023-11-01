package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findRoomById(Long id);

    List<Room> findRoomByRoomNumber(int roomNumber);

    List<Room> findAllByRoomType(RoomType roomType);

    @Query("SELECT r FROM Room r JOIN r.hotel h WHERE h.city = :city")
    List<Room> findRoomsLike(@Param("city") String city);

    @Query("SELECT r FROM Room r WHERE" +
            "(:singleBeds IS NULL OR r.singleBeds = :singleBeds)" +
            "AND (:doubleBeds IS NULL OR r.doubleBeds = :doubleBeds)")
    List<Room> findRoomsWithConditions(@Param("singleBeds") int singleBeds, @Param("doubleBeds") int doubleBeds);

}
