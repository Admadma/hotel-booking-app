package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;
    private RoomDTO room;
    private UserModel user;
    private LocalDate startDate;
    private LocalDate endDate;
}
