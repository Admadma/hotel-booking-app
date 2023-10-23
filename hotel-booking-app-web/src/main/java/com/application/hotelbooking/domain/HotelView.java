package com.application.hotelbooking.domain;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelView {
    private Long id;
    @Size(min = 2, message = "{admin.hotel.validation.name.size.too_short}")
    private String hotelName;
    private String city;
    private List<Room> rooms;
}
