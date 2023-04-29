package com.application.hotelbooking.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomView {

    @Null
    private Long id;
    @Null
    private Long version;

    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int pricePerNight;
    @NotNull
    private RoomType roomType;
    @Null
    private List<ReservationView> reservations;
}
