package com.project.lecture.api.Listen.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionExistCourse;
import com.project.lecture.exception.kind.ExceptionNotFoundCourse;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.user.service.MemberService;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListenApplicationTest {

  @Mock
  private ListenService listenService;
  @Mock
  private MemberService memberService;
  @Mock
  private PlannerService plannerService;
  @Mock
  private CourseService courseService;
  @InjectMocks
  private ListenApplication listenApplication;

  @Test
  @DisplayName("회원의 강의 수강 - 성공")
  void listenCourse() {
    Member member = CommonHelper.createMemberForm();
    Long courseId = 1L;
    String email = "planner@gmail.com";
    // given
    when(memberService.getMemberByEmail(any()))
        .thenReturn(member);
    when(listenService.existCheck(anyLong(), anyLong()))
        .thenReturn(false);
    doNothing().when(listenService).saveListening(any());

    // when
    listenApplication.listenCourse(courseId, email);

    // then
    verify(memberService, timeout(1)).getMemberByEmail(any());
    verify(listenService, timeout(1)).existCheck(anyLong(), anyLong());
    verify(listenService, timeout(1)).saveListening(any());
  }

  @Test
  @DisplayName("회원의 강좌 수강 - 실패[이미 수강 중]")
  void listenCourse_fail() {
    Member member = CommonHelper.createMemberForm();
    Long courseId = 1L;
    String email = "planner@gmail.com";
    // given
    when(memberService.getMemberByEmail(any()))
        .thenReturn(member);
    when(listenService.existCheck(anyLong(), anyLong()))
        .thenReturn(true);

    // when
    SuperException result = Assert.assertThrows(ExceptionExistCourse.class,
        () -> listenApplication.listenCourse(courseId, email));

    // then
    assertEquals(result.getMessage(), "이미 수강 중인 강좌입니다.");
    verify(memberService, timeout(1)).getMemberByEmail(any());
    verify(listenService, timeout(1)).existCheck(anyLong(), anyLong());
  }

  @Test
  @DisplayName("회원의 강좌 수강 취소 - 성공[planner가 없는 경우]")
  void deleteListenCourse_success() {
    Member member = CommonHelper.createMemberForm();
    String email = "planner@gmail.com";
    Long courseId = 1L;
    // given
    when(memberService.getMemberByEmail(any()))
        .thenReturn(member);
    when(listenService.existCheck(anyLong(), anyLong()))
        .thenReturn(true);
    doNothing().when(listenService).deleteListing(anyLong(), anyLong());

    // when
    listenApplication.deleteListenCourse(courseId, email);

    //then
    verify(memberService, timeout(1)).getMemberByEmail(any());
    verify(listenService, timeout(1)).existCheck(anyLong(), anyLong());
    verify(listenService, timeout(1)).deleteListing(anyLong(), anyLong());
  }

  @Test
  @DisplayName("회원의 강좌 수강 취소 - 실패[이미 수강이 취소된 경우]")
  void deleteListenCourse_fail() {
    Member member = CommonHelper.createMemberForm();
    String email = "planner@gmail.com";
    Long courseId = 1L;
    // given
    when(memberService.getMemberByEmail(any()))
        .thenReturn(member);
    when(listenService.existCheck(anyLong(), anyLong()))
        .thenReturn(false);

    // when
    SuperException result = Assert.assertThrows(ExceptionNotFoundCourse.class,
        () -> listenApplication.deleteListenCourse(courseId, email));

    //then
    assertEquals(result.getMessage(), "강좌가 존재하지 않습니다.");
    verify(memberService, timeout(1)).getMemberByEmail(any());
    verify(listenService, timeout(1)).existCheck(anyLong(), anyLong());
  }

  @Test
  @DisplayName("회원의 강좌 수강 취소 - 성공[planner가 있는 경우]")
  void deleteListenCourse_success_exist_planner() {
    Member member = CommonHelper.createMemberAndPlannersForm();
    Course lectures = CommonHelper.createCourseForm();
    String email = "planner@gmail.com";
    Long courseId = 1L;
    // given
    when(memberService.getMemberByEmail(any()))
        .thenReturn(member);
    when(listenService.existCheck(anyLong(), anyLong()))
        .thenReturn(true);
    when(listenService.existCheck(anyLong(), anyLong()))
        .thenReturn(true);
    doNothing().when(listenService).deleteListing(anyLong(), anyLong());
    doNothing().when(plannerService).deleteIfExistCourse(anyLong(), anyList());
    when(courseService.getCourseById(anyLong()))
        .thenReturn(lectures);
    doNothing().when(plannerService).deleteIfExistLecture(anyLong(), anyList());

    // when
    listenApplication.deleteListenCourse(courseId, email);

    //then
    verify(memberService, timeout(1)).getMemberByEmail(any());
    verify(listenService, timeout(1)).existCheck(anyLong(), anyLong());
    verify(listenService, timeout(1)).deleteListing(anyLong(), anyLong());
    verify(plannerService, timeout(1)).deleteIfExistCourse(anyLong(), anyList());
    verify(courseService, timeout(1)).getCourseById(anyLong());
    verify(plannerService, timeout(1)).deleteIfExistLecture(anyLong(), anyList());
  }
}