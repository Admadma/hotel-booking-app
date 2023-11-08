package com.application.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelCreationServiceDTO {
    private String hotelName;
    private String city;
}
