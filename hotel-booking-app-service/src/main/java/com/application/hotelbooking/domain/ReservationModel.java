package com.application.hotelbooking.domain;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationModel {

    private Long id;
    private RoomModel room;
    private UserModel user;
    private LocalDate startDate;
    private LocalDate endDate;
}
