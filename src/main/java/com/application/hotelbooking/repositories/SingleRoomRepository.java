package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.SingleRoom;
import org.springframework.stereotype.Repository;

@Repository("singleRoomRepository")
public interface SingleRoomRepository extends RoomBaseRepository<SingleRoom> {
}
