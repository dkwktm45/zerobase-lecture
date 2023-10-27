package com.project.lecture.course.application;

import static com.project.lecture.type.ResponseType.COURSE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.UserHelper;
import com.project.lecture.course.dto.CourseRequest;
import com.project.lecture.course.service.CourseService;
import com.project.lecture.course.service.LectureService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.user.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseApplicationTest {

  @Mock
  private CourseService courseService;
  @Mock
  private MemberService memberService;
  @Mock
  private LectureService lectureService;
  @InjectMocks
  private CourseApplication courseApplication;

  @Test
  @DisplayName("강좌 생성시 관리자 유저를 넣는다. - 성공")
  void createCourseAndLecture() {
    Member member = UserHelper.createMemberForm();
    Course course = UserHelper.createCourseForm();
    CourseRequest.Create req = UserHelper.createCourseCreateForm();

    // given
    when(memberService.getEmail(anyString()))
        .thenReturn(member);
    when(courseService.createCourse(any()))
        .thenReturn(course);
    doNothing().when(lectureService).ListInsert(any());

    // when
    String response = courseApplication.createCourseAndLecture(req, member.getEmail());

    // then
    assertEquals(response,COURSE_SUCCESS.getDescription());
    verify(memberService, timeout(1)).getEmail(any());
    verify(courseService, timeout(1)).createCourse(any());
    verify(lectureService, timeout(1)).ListInsert(any());
  }

}