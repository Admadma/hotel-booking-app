package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
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

    @NotNull(message = "{home.room.form.validation.startdate.not.null}")
    @FutureOrPresent(message = "{home.room.form.validation.startdate.future.or.present}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "{home.room.form.validation.enddate.not.null}")
    @Future(message = "{home.room.form.validation.enddate.future}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
