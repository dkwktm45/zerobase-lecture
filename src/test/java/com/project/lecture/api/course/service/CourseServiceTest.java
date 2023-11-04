package com.project.lecture.api.course.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.course.dto.CourseRequest;
import com.project.lecture.api.course.dto.CourseRequest.Change;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.SuperException;
import com.project.lecture.repository.CourseRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

  @Mock
  private CourseRepository courseRepository;
  @InjectMocks
  private CourseService courseService;

  @Test
  @DisplayName("강좌 생성")
  void createCourse() {
    CourseRequest.Create arg = CommonHelper.createCourseCreateForm();
    Course course = CommonHelper.createCourseForm();
    Member member = CommonHelper.createMemberForm();
    when(courseRepository.save(any()))
        .thenReturn(course);

    Course result = courseService.createCourse(arg, member);

    assertEquals(result.getCourseContent(), course.getCourseContent());
    assertEquals(result.getCourseName(), course.getCourseName());
    for (int i = 0; i < course.getLectures().size(); i++) {
      assertEquals(course.getLectures().get(i)
          , result.getLectures().get(i));
    }
    verify(courseRepository, timeout(1)).save(any());
  }

  @Test
  @DisplayName("강좌 삭제")
  void deleteCourseAndLectureById() {
    //given
    doNothing().when(courseRepository).deleteById(anyLong());
    //when
    courseService.deleteCourseAndLectureById(1L);
    //then
    verify(courseRepository, timeout(1)).deleteById(anyLong());
  }

  @Test
  @DisplayName("강좌 수정 - 성공")
  void changeCourse_success() {
    //given
    Change req = CommonHelper.changeCourseForm();
    Course course = CommonHelper.createOnlyCourseForm();
    when(courseRepository.findById(any()))
        .thenReturn(Optional.of(course));
    //when
    courseService.changeCourseByForm(req);
    //then
    verify(courseRepository, timeout(1)).findById(any());
  }

  @Test
  @DisplayName("강좌 수정 실패[NotFound]")
  void changeCourse_fail() {
    //given
    Change req = CommonHelper.changeCourseForm();
    when(courseRepository.findById(any()))
        .thenReturn(Optional.empty());
    //when
    SuperException exception = assertThrows(SuperException.class,
        () -> courseService.changeCourseByForm(req));

    // then
    assertEquals(exception.getMessage(),"강좌가 존재하지 않습니다.");
    verify(courseRepository, timeout(1)).findById(any());
  }
}