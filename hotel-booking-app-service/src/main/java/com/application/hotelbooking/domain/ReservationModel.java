package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationModel {

    private Long id;
    private RoomModel room;
    private UserModel user;
    private int totalPrice;
    private LocalDate startDate;
    private LocalDate endDate;
}
