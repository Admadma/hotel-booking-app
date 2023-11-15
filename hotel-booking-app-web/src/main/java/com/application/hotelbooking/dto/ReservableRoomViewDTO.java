package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservableRoomViewDTO {

    private Long version;
    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int totalPrice;
    private RoomType roomType;
    private String hotelName;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
}
