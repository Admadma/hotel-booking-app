package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPlanDTO {

    private String hotelName;
    private String city;
    private RoomType roomType;
    private int singleBeds;
    private int doubleBeds;
    private LocalDate startDate;
    private LocalDate endDate;
    private int pricePerNight;
    private int totalPrice;
}