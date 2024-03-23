package com.application.hotelbooking.services;

import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    List<ReservableRoomDTO> searchRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO);
    boolean isEndDateAfterStartDate(LocalDate startDate, LocalDate endDate);
}
