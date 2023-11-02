package com.application.hotelbooking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSelectedDTO {

    @Positive(message = "{admin.room.validation.roomnumber.positive}")
    private int roomNumber;
    @NotNull(message = "Must not be empty")
    private String hotelName;
}
