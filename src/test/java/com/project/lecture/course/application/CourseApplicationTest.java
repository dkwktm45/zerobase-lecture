package com.project.lecture.course.application;

import static com.project.lecture.type.ResponseType.COURSE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.UserHelper;
import com.project.lecture.course.dto.CourseRequest;
import com.project.lecture.course.service.CourseService;
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

    String response = courseApplication.createCourseAndLecture(req, member.getEmail());

    assertEquals(response,COURSE_SUCCESS.getDescription());
  }
}