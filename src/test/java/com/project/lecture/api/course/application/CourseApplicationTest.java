package com.project.lecture.api.course.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.course.dto.CourseDto;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class CourseApplicationTest {

  @Mock
  private CourseService courseService;
  @InjectMocks
  private CourseApplication courseApplication;
  @Test
  @DisplayName("강좌 리스트를 반환 - 성공")
  void getCourseList(){
    //given
    Member member = CommonHelper.createMemberForm();

    List<Course> courses = member.getCourses();
    Page<Course> coursePage = new PageImpl<>(courses);
    when(courseService.getListByEmailAndPage(anyString(),any()))
        .thenReturn(coursePage);

    //when
    Page<CourseDto> courseDtoPage = courseApplication.getCoursesList(any());

    //then
    List<CourseDto> result = courseDtoPage.getContent();
    for (int i = 0; i < courses.size(); i++) {
      assertEquals(result.get(i).getCourseId(), courses.get(i).getCourseId());
      assertEquals(result.get(i).getCourseContent(), courses.get(i).getCourseContent());
      assertEquals(result.get(i).getCourseName(), courses.get(i).getCourseName());
    }
  }
}