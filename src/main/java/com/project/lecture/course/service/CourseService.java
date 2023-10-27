package com.project.lecture.course.service;

import com.project.lecture.course.dto.CourseRequest.Create;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;

  public Course createCourse(Create request, Member member) {
    Course course = request.toEntity(request);
    course.setMember(member);

    return courseRepository.save(
          course
        );
  }

  public void deleteCourseAndLectureById(Long courseId) {
    courseRepository.deleteById(courseId);
  }

  public void deleteCourseAndLectureById(Long courseId) {
    courseRepository.deleteById(courseId);
  }
}
