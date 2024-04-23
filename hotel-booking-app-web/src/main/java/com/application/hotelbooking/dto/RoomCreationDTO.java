package com.application.hotelbooking.dto;

import com.application.hotelbooking.views.RoomType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreationDTO {

    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private int singleBeds;
    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private int doubleBeds;
    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private int pricePerNight;
    @NotNull
    private RoomType roomType;
    @NotNull
    private Long hotelId;
}