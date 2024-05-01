package com.application.hotelbooking.services;

import java.time.LocalDate;
import java.util.List;

public interface AvailableRoomsFilterService {

    List<Long> filterFreeRooms(List<Long> roomIds, LocalDate startDate, LocalDate endDate);
}
