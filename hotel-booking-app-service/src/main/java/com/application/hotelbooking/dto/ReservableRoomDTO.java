package com.application.hotelbooking.dto;

import com.application.hotelbooking.models.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservableRoomDTO {

    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int totalPrice;
    private RoomType roomType;
    private String hotelName;
    private String city;
    private String imageName;
    private LocalDate startDate;
    private LocalDate endDate;
}
