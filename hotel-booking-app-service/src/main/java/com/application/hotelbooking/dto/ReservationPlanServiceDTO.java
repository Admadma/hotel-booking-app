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
public class ReservationPlanServiceDTO {

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
