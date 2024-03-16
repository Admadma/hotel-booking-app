package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationView {

    private Long id;
    private RoomView room;
    private UserView user;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalPrice;
}
