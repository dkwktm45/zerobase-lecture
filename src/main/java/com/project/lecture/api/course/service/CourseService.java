package com.project.lecture.api.course.service;

import com.project.lecture.api.course.dto.CourseRequest.Change;
import com.project.lecture.api.course.dto.CourseRequest.Create;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.ExceptionNotFoundCourse;
import com.project.lecture.repository.CourseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public void changeCourseByForm(Change course) {
    Course fromEntity = courseRepository
        .findById(course.getCourseId())
        .orElseThrow(ExceptionNotFoundCourse::new);

    fromEntity.changeValues(Change
        .toEntity(course));
  }

  public Course getCourseById(Long courseId) {
    return courseRepository.findById(courseId)
        .orElseThrow(ExceptionNotFoundCourse::new);
  }

  public Page<Course> getListByEmailAndPage(String email, Pageable pageable) {
    return courseRepository.findByMember_Email(email,pageable);
  }

  public Course getCourseByLectureId(Long lectureId) {
    return courseRepository.findByLectures_LectureId(lectureId)
        .orElseThrow(ExceptionNotFoundCourse::new);
  }
  public Page<Course> getListByPage(Pageable pageable) {
    return courseRepository.findAll(pageable);
  }

  public List<Course> getListByMember(Member member) {
    return courseRepository.findAllByListenings_Member(member);
  }
}
