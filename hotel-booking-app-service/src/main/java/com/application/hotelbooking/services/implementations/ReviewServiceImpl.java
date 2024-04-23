package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.models.HotelModel;
import com.application.hotelbooking.models.ReviewModel;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.services.ReviewService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.services.repositoryservices.ReviewRepositoryService;
import com.application.hotelbooking.services.repositoryservices.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepositoryService reviewRepositoryService;

    @Autowired
    private HotelRepositoryService hotelRepositoryService;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private HotelService hotelService;

    public ReviewModel createReview(int rating, String comment, String hotelName, String userName){
        HotelModel hotelModel = hotelRepositoryService.findHotelByHotelName(hotelName).get();
        ReviewModel reviewModel = ReviewModel.builder()
                .rating(rating)
                .comment(comment)
                .hotel(hotelModel)
                .user(userRepositoryService.getUserByName(userName).get())
                .build();

        ReviewModel resultReview = reviewRepositoryService.save(reviewModel);
        hotelService.updateAverageRating(hotelModel);
        return resultReview;
    }
}
