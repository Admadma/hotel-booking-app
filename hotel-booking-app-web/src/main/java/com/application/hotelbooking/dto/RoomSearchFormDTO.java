package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchFormDTO {
    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private int singleBeds;
    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private int doubleBeds;
    @NotNull
    private RoomType roomType;
    private String city;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
