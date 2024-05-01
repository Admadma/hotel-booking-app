package com.application.hotelbooking.services.repositoryservices.implementations;

import com.application.hotelbooking.models.ReviewModel;
import com.application.hotelbooking.repositories.ReviewRepository;
import com.application.hotelbooking.services.repositoryservices.ReviewRepositoryService;
import com.application.hotelbooking.transformers.ReviewTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewRepositoryServiceImpl implements ReviewRepositoryService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewTransformer reviewTransformer;

    public ReviewModel save(ReviewModel reviewModel) {
        return reviewTransformer.transformToReviewModel(reviewRepository.save(reviewTransformer.transformToReview(reviewModel)));
    }
}
