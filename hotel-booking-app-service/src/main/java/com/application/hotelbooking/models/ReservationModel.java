package com.application.hotelbooking.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationModel {

    private Long id;
    private UUID uuid;
    private RoomModel room;
    private UserModel user;
    private int totalPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus reservationStatus;
}
