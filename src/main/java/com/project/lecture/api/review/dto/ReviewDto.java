package com.project.lecture.api.review.dto;

import com.project.lecture.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
  private Long reviewId;
  private String reviewContent;
  private int reviewRating;
  private String createdEmail;
  private String createDt;

  public ReviewDto(Review review) {
    this.reviewId = review.getReviewId();
    this.reviewContent = review.getReviewContent();
    this.reviewRating = review.getReviewRating();
    this.createdEmail = review.getCreatedEmail();
    this.createDt = review.getCreateDt();
  }

  public static Page<ReviewDto> toDtoList(Page<Review> studies){
    return studies.map(ReviewDto::new);
  }
}
