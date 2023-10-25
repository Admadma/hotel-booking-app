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
    @Positive(message = "{admin.room.validation.roomnumber.positive}")
    private int roomNumber;
    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private int singleBeds;
    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private int doubleBeds;
    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private int pricePerNight;
    @NotNull
    private RoomType roomType;
    @Null
    private List<ReservationView> reservations;
}
