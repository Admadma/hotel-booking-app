package com.application.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelWithReservableRoomsServiceDTO {
    private String hotelName;
    private String city;
    private String imageName;
    private Double averageRating;
    private List<UniqueReservableRoomOfHotelServiceDTO> uniqueReservableRoomOfHotelServiceDTOList;
}
