package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
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
    private int totalPrice;
    private RoomType roomType;
    private LocalDate startDate;
    private LocalDate endDate;
}
