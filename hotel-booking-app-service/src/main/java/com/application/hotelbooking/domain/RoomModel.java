package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomModel {

    private Long id;
    private Long version;
    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int pricePerNight;
    private RoomType roomType;
    private List<ReservationModel> reservations;
    private HotelModel hotel;
}
