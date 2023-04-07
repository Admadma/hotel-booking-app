package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RoomBaseRepository<T extends Room> extends JpaRepository<T, Long> {
    T findRoomById(Long id);

}
