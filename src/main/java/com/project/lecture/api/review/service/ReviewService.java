package com.project.lecture.api.review.service;

import com.project.lecture.entity.Review;
import com.project.lecture.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

  private final ReviewRepository reviewRepository;


  public void saveEntity(Review review) {
    reviewRepository.save(review);
  }


  public Page<Review> getListByCourseIdAndPage(Long id, Pageable pageable) {
    return reviewRepository.findByCourse_CourseId(id,pageable);
  }
}
