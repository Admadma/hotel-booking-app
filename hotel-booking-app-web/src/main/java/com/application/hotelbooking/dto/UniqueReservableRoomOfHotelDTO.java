package com.application.hotelbooking.dto;

import com.application.hotelbooking.views.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueReservableRoomOfHotelDTO {

    private int number;
    private int singleBeds;
    private int doubleBeds;
    private int pricePerNight;
    private int totalPrice;
    private RoomType roomType;
    private LocalDate startDate;
    private LocalDate endDate;
}
