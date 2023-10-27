package com.project.lecture.course.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.UserHelper;
import com.project.lecture.course.dto.CourseRequest;
import com.project.lecture.entity.Course;
import com.project.lecture.repository.CourseRepository;
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
    CourseRequest.Create arg = UserHelper.createCourseCreateForm();
    Course course = UserHelper.createCourseForm();

    when(courseRepository.save(any()))
        .thenReturn(course);

    Course result = courseService.createCourse(arg);

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
}