package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreationDTO {

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
    @NotNull
    private String hotelId;
}