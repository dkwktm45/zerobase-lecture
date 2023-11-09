package com.project.lecture.api.course.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.course.dto.CourseDto;
import com.project.lecture.api.course.dto.CourseRequest;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.user.service.MemberService;
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
class AdminCourseApplicationTest {

  @Mock
  private CourseService courseService;
  @Mock
  private MemberService memberService;
  @Mock
  private LectureService lectureService;
  @InjectMocks
  private AdminCourseApplication adminCourseApplication;

  @Test
  @DisplayName("강좌 생성시 관리자 유저를 넣는다. - 성공")
  void createCourseAndLecture() {
    Member member = CommonHelper.createMemberForm();
    Course course = CommonHelper.createCourseForm();
    CourseRequest.Create req = CommonHelper.createCourseCreateForm();

    // given
    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    when(courseService.createCourse(any(),any()))
        .thenReturn(course);
    doNothing().when(lectureService).ListInsert(any());

    adminCourseApplication.createCourseAndLecture(req, member.getEmail());

    // then
    verify(memberService, timeout(1)).getMemberByEmail(any());
    verify(courseService, timeout(1)).createCourse(any(),any());
    verify(lectureService, timeout(1)).ListInsert(any());
  }

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
    Page<CourseDto> courseDtoPage = adminCourseApplication.getCourseList(anyString(),any());

    //then
    List<CourseDto> result = courseDtoPage.getContent();
    for (int i = 0; i < courses.size(); i++) {
      assertEquals(result.get(i).getCourseId(), courses.get(i).getCourseId());
      assertEquals(result.get(i).getCourseContent(), courses.get(i).getCourseContent());
      assertEquals(result.get(i).getCourseName(), courses.get(i).getCourseName());
    }
  }
}