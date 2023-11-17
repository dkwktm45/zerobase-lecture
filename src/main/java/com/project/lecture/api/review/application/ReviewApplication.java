package com.project.lecture.api.review.application;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.review.dto.ReviewDto;
import com.project.lecture.api.review.dto.ReviewRequest.Create;
import com.project.lecture.api.review.service.ReviewService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Review;
import com.project.lecture.exception.kind.ExceptionExistListening;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewApplication {

  private final MemberService memberService;
  private final ReviewService reviewService;
  private final CourseService courseService;
  private final ListenService listenService;

  public void createByDtoAndEmail(Create request, String email) {
    Member member = memberService.getMemberByEmail(email);
    if (!listenService.existCheck(request.getCourseId(),member.getMemberId())) {
      throw new ExceptionExistListening();
    }
    Course course = courseService.getCourseById(request.getCourseId());

    reviewService.saveEntity(Review.builder()
        .createdEmail(email)
        .course(course)
        .reviewContent(request.getReviewContent())
        .reviewRating(request.getReviewRating())
        .build());
  }

  public Page<ReviewDto> getReviewListById(Long courseId, Pageable pageable) {
    Page<Review> reviews = reviewService.getListByCourseIdAndPage(courseId, pageable);
    return ReviewDto.toDtoList(reviews);
  }
}
