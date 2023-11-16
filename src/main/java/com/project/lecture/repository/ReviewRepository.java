package com.project.lecture.repository;

import com.project.lecture.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {

  Page<Review> findByCourse_CourseId(Long id, Pageable pageable);
}
