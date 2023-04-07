package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.FamilyRoom;
import org.springframework.stereotype.Repository;

@Repository("familyRoomRepository")
public interface FamilyRoomRepository extends RoomBaseRepository<FamilyRoom>{
}
