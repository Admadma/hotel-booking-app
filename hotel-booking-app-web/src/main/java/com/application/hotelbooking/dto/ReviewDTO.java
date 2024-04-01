package com.application.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    @Range(min = 1, max = 5, message = "{review.error.rating.out.of.bounds}")
    private int rating;
    @Length(max = 300, message = "{review.error.comment.too.long}")
    private String comment;
}
