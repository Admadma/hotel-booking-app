package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findRoomById(Long id);

    Room findRoomByRoomNumber(int roomNumber);

//    @Query(
//            value = "SELECT * FROM rooms r WHERE r.room_type = ?1",
//            nativeQuery = true)
//    List<Room> findAllRoomsOfGivenType(String roomType);

    List<Room> findAllByRoomType(RoomType roomType);
}
