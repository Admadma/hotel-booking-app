package com.application.hotelbooking.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class HotelView {
    @Null
    private Long id;
    @Size(min = 2, message = "{admin.hotel.validation.name.size.too_short}")
    private String hotelName;
    @NotEmpty(message = "{admin.hotel.validation.city.empty}")
    private String city;
    @Null
    private List<Room> rooms;
}
