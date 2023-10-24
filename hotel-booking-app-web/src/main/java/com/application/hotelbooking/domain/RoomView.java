package com.application.hotelbooking.domain;

import jakarta.validation.constraints.*;
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
    @Positive
    private int roomNumber;
    @PositiveOrZero
    private int singleBeds;
    @PositiveOrZero
    private int doubleBeds;
    @PositiveOrZero
    private int pricePerNight;
    @NotNull
    private RoomType roomType;
    @Null
    private List<ReservationView> reservations;
}
