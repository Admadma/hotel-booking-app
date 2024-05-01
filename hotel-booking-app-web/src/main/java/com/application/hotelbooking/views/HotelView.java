package com.application.hotelbooking.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelView {
    private Long id;
    private String hotelName;
    private String city;
    private List<RoomView> rooms;
    private String imageName;
}
