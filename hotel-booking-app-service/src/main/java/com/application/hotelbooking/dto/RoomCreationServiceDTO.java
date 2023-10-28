package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreationServiceDTO {

    private Long version;
    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int pricePerNight;
    private RoomType roomType;
    private String hotelId;
}
