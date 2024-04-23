package com.application.hotelbooking.services;

import com.application.hotelbooking.models.ReviewModel;

public interface ReviewService {

    ReviewModel createReview(int rating, String comment, String hotelName, String userName);
}
