package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewModel {

    private int rating;
    private String comment;
    private HotelModel hotel;
    private UserModel user;

    @Override
    public String toString() {
        return "ReviewModel{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", hotel=" + hotel.getHotelName() +
                '}';
    }
}
