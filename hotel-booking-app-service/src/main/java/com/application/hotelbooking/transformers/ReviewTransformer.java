package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Review;
import com.application.hotelbooking.domain.ReviewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public Review transformToReview(ReviewModel reviewModel){
        return modelMapper.map(reviewModel, Review.class);
    }

    public ReviewModel transformToReviewModel(Review review){
        return modelMapper.map(review, ReviewModel.class);
    }
}
