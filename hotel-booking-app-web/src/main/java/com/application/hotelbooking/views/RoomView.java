package com.application.hotelbooking.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomView {

    private Long id;
    private Long version;
    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int pricePerNight;
    private RoomType roomType;
    private List<ReservationView> reservations;
    private HotelView hotel;
}
