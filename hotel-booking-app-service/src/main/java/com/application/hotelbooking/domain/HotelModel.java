package com.application.hotelbooking.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelModel {

    private String hotelName;
    private String city;
    private List<RoomModel> rooms;
}
