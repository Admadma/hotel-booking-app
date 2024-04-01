package com.application.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelWithReservableRoomsServiceDTO {
    private String hotelName;
    private String city;
    private String imageName;
    private List<UniqueReservableRoomOfHotelServiceDTO> uniqueReservableRoomOfHotelServiceDTOList;
}
