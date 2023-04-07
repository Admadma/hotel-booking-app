package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findRoomById(Long id);
}
