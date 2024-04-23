package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.entities.Review;
import com.application.hotelbooking.models.ReviewModel;
import com.application.hotelbooking.repositories.ReviewRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.ReviewRepositoryServiceImpl;
import com.application.hotelbooking.transformers.ReviewTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewRepositoryServiceImplTest {

    private static final ReviewModel REVIEW_MODEL = ReviewModel.builder().rating(5).build();
    private static final Review REVIEW = Review.builder().rating(5).build();

    @InjectMocks
    private ReviewRepositoryServiceImpl reviewRepositoryServiceImpl;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewTransformer reviewTransformer;

    @Test
    public void testSaveShouldReturnHotelModelOfSavedHotel() {
        when(reviewTransformer.transformToReview(REVIEW_MODEL)).thenReturn(REVIEW);
        when(reviewRepository.save(REVIEW)).thenReturn(REVIEW);
        when(reviewTransformer.transformToReviewModel(REVIEW)).thenReturn(REVIEW_MODEL);

        ReviewModel savedReview = reviewRepositoryServiceImpl.save(REVIEW_MODEL);

        verify(reviewTransformer).transformToReview(REVIEW_MODEL);
        verify(reviewRepository).save(REVIEW);
        verify(reviewTransformer).transformToReviewModel(REVIEW);
        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getRating()).isEqualTo(5);
    }
}
