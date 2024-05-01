package com.application.hotelbooking.transformers;

import com.application.hotelbooking.entities.Review;
import com.application.hotelbooking.models.ReviewModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewTransformerTest {

    public static final Class<Review> REVIEW_CLASS = Review.class;
    public static final Class<ReviewModel> REVIEW_MODEL_CLASS = ReviewModel.class;

    private static final Review REVIEW = new Review();
    private static final ReviewModel REVIEW_MODEL = new ReviewModel();

    @InjectMocks
    private ReviewTransformer reviewTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToReviewShouldReturnTransformedReviewModel(){
        when(modelMapper.map(REVIEW_MODEL, REVIEW_CLASS)).thenReturn(REVIEW);

        Review resultReview = reviewTransformer.transformToReview(REVIEW_MODEL);

        verify(modelMapper).map(REVIEW_MODEL, REVIEW_CLASS);
        Assertions.assertThat(resultReview).isNotNull();
        Assertions.assertThat(resultReview).isEqualTo(REVIEW);
    }

    @Test
    public void testTransformToReviewModelShouldReturnTransformedReview(){
        when(modelMapper.map(REVIEW, REVIEW_MODEL_CLASS)).thenReturn(REVIEW_MODEL);

        ReviewModel resultReviewModel = reviewTransformer.transformToReviewModel(REVIEW);

        verify(modelMapper).map(REVIEW, REVIEW_MODEL_CLASS);
        Assertions.assertThat(resultReviewModel).isNotNull();
        Assertions.assertThat(resultReviewModel).isEqualTo(REVIEW_MODEL);
    }
}
