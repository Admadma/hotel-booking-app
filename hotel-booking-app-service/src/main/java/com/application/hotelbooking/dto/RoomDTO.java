package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long version;
    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int pricePerNight;
    private RoomType roomType;
    private List<ReservationDTO> reservations;
    private HotelDTO hotel;
}
