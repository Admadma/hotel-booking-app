package com.application.hotelbooking.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelModel {

    private Long id;
    private String hotelName;
    private String city;
    private List<RoomModel> rooms;
    private String imageName;
    private List<ReviewModel> reviews;
    private Double averageRating;
}
