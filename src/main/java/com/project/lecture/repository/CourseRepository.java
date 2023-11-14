package com.project.lecture.repository;

import com.project.lecture.entity.Course;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {

  Page<Course> findByMember_Email(String email, Pageable pageable);

  Optional<Course> findByLectures_LectureId(Long lectureId);
}
