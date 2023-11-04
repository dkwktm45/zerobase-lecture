package com.project.lecture.repository;

import com.project.lecture.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {

  Page<Course> findByMember_Email(String email, Pageable pageable);
}
