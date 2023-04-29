package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationView {

    private Long id;
    private RoomView room;
    private UserModel user;
    private LocalDate startDate;
    private LocalDate endDate;
}