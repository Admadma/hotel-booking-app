package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findRoomById(Long id);
}
