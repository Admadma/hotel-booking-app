package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelsWithReservableRoomsDTO {
    private String hotelName;
    private String city;
    private List<UniqueReservableRoomOfHotelDTO> uniqueReservableRooms;
}
