package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
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
public class RoomSearchFormServiceDTO {
    private Integer singleBeds;
    private Integer doubleBeds;
    private RoomType roomType;
    private String hotelName;
    private String city;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
