package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomModel {

    private Long id;

    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private RoomType roomType;
    private List<ReservationModel> reservations;
}
