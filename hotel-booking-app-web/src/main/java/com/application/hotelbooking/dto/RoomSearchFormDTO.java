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
    private Integer singleBeds;
    @PositiveOrZero(message = "{admin.room.validation.roomnumber.positiveorzero}")
    private Integer doubleBeds;
    private RoomType roomType;
    private String hotelName;
    private String city;

    //TODO: add min-max prices

    //TODO: uncomment this after done debugging the other fields
//    @NotNull
//    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

//    @NotNull
//    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
