package com.project.lecture.api.complete.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.complete.service.CourseLectureService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionExistListening;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompleteApplicationTest {

  @Mock
  private CourseService courseService;
  @Mock
  private LectureService lectureService;
  @Mock
  private ListenService listenService;
  @Mock
  private MemberService memberService;
  @Mock
  private CourseLectureService courseLectureService;

  @InjectMocks
  private CompleteApplication completeApplication;

  @Test
  @DisplayName("강좌를 수강완료한 경우 티어가 갱신되고 MemberCourseLecturer값이 저장되는 로직 - 성공[존재 O]")
  void completeCourseByIdAndEmail_success_exist() {
    //given
    Member member = CommonHelper.createOriginMemberFormByNoId();
    Course course = CommonHelper.createCourseForm();
    MemberCourseLecture memberCourseLecture = CommonHelper.createMemberCourseLecture();
    Long id = 1L;
    String email = "wpekdl153@gmail.com";
    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    when(courseService.getCourseById(anyLong()))
        .thenReturn(course);
    when(listenService.existCheckByMemberAndCourse(any(), any()))
        .thenReturn(true);
    when(courseLectureService.existCourseIdByMemberAndId(member, id))
        .thenReturn(true);
    when(courseLectureService.getCourseLectureByMemberAndId(any(), anyLong()))
        .thenReturn(Optional.of(memberCourseLecture));
    //when
    completeApplication.completeCourseByIdAndEmail(id, email);

    //then
    verify(memberService, timeout(1)).getMemberByEmail(anyString());
    verify(courseService, timeout(1)).getCourseById(anyLong());
    verify(listenService, timeout(1)).existCheckByMemberAndCourse(any(), any());
    verify(courseLectureService, timeout(1)).existCourseIdByMemberAndId(any(), anyLong());
    verify(courseLectureService, timeout(1)).getCourseLectureByMemberAndId(any(), anyLong());
    verify(courseLectureService, never()).saveEntity(any());
  }

  @Test
  @DisplayName("강좌를 수강완료한 경우 티어가 갱신되고 MemberCourseLecturer값이 저장되는 로직 - 성공[존재 X]")
  void completeCourseByIdAndEmail_success_not_exist() {
    //given
    Member member = CommonHelper.createOriginMemberFormByNoId();
    Course course = CommonHelper.createCourseForm();
    Long id = 1L;
    String email = "wpekdl153@gmail.com";
    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    when(courseService.getCourseById(anyLong()))
        .thenReturn(course);
    when(listenService.existCheckByMemberAndCourse(any(), any()))
        .thenReturn(true);
    when(courseLectureService.existCourseIdByMemberAndId(member, id))
        .thenReturn(false);
    doNothing().when(courseLectureService).saveEntity(any());
    //when
    completeApplication.completeCourseByIdAndEmail(id, email);

    //then
    verify(memberService, timeout(1)).getMemberByEmail(anyString());
    verify(courseService, timeout(1)).getCourseById(anyLong());
    verify(listenService, timeout(1)).existCheckByMemberAndCourse(any(), any());
    verify(courseLectureService, timeout(1)).existCourseIdByMemberAndId(any(), anyLong());
    verify(courseLectureService, never()).getCourseLectureByMemberAndId(any(), anyLong());
    verify(courseLectureService, timeout(1)).saveEntity(any());
  }

  @Test
  @DisplayName("강좌를 수강완료한 경우 티어가 갱신되고 MemberCourseLecturer값이 저장되는 로직 - 실패]")
  void completeCourseByIdAndEmail_fail() {
    //given
    Member member = CommonHelper.createOriginMemberFormByNoId();
    Course course = CommonHelper.createCourseForm();
    Long id = 1L;
    String email = "wpekdl153@gmail.com";
    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    when(courseService.getCourseById(anyLong()))
        .thenReturn(course);
    when(listenService.existCheckByMemberAndCourse(any(), any()))
        .thenReturn(false);
    //when
    SuperException result = assertThrows(
        ExceptionExistListening.class,
        () -> completeApplication.completeCourseByIdAndEmail(id, email));

    //then
    assertEquals(result.getMessage(), "수강 중이지 않는 강좌입니다.");
    verify(memberService, timeout(1)).getMemberByEmail(anyString());
    verify(courseService, timeout(1)).getCourseById(anyLong());
    verify(listenService, timeout(1)).existCheckByMemberAndCourse(any(), any());
    verify(courseLectureService, never()).existCourseIdByMemberAndId(any(), anyLong());
    verify(courseLectureService, never()).getCourseLectureByMemberAndId(any(), anyLong());
    verify(courseLectureService, never()).saveEntity(any());
  }

  @Test
  @DisplayName("강의를 수강한 경우 티어가 갱신되고 MemberCourseLecturer값이 저장되는 로직 - 성공[존재 O]")
  void completeLectureByIdAndEmail_success_exist() {
    //given
    Member member = CommonHelper.createOriginMemberFormByNoId();
    Lecture lecture = CommonHelper.createLecture();
    MemberCourseLecture memberCourseLecture = CommonHelper.createMemberCourseLecture();
    Long id = 1L;
    String email = "wpekdl153@gmail.com";
    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    when(lectureService.getLectureById(anyLong()))
        .thenReturn(lecture);
    when(listenService.existCheckByMemberAndCourse(any(), any()))
        .thenReturn(true);
    when(courseLectureService.existCourseIdByMemberAndId(any(), anyLong()))
        .thenReturn(true);
    when(courseLectureService.getCourseLectureByMemberAndId(any(), anyLong()))
        .thenReturn(Optional.of(memberCourseLecture));

    //then
    completeApplication.completeLectureByIdAndEmail(id, email);

    //then
    verify(memberService, timeout(1)).getMemberByEmail(anyString());
    verify(lectureService, timeout(1)).getLectureById(anyLong());
    verify(listenService, timeout(1)).existCheckByMemberAndCourse(any(), any());
    verify(courseLectureService, timeout(1)).existCourseIdByMemberAndId(any(), anyLong());
    verify(courseLectureService, timeout(1)).getCourseLectureByMemberAndId(any(), anyLong());
    verify(courseLectureService, never()).saveEntity(any());
  }
  @Test
  @DisplayName("강의를 수강한 경우 티어가 갱신되고 MemberCourseLecturer값이 저장되는 로직 - 성공[존재 X]")
  void completeLectureByIdAndEmail_success_not_exist() {
    //given
    Member member = CommonHelper.createOriginMemberFormByNoId();
    Lecture lecture = CommonHelper.createLecture();
    Long id = 1L;
    String email = "wpekdl153@gmail.com";
    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    when(lectureService.getLectureById(anyLong()))
        .thenReturn(lecture);
    when(listenService.existCheckByMemberAndCourse(any(), any()))
        .thenReturn(true);
    when(courseLectureService.existCourseIdByMemberAndId(any(), anyLong()))
        .thenReturn(false);
    doNothing().when(courseLectureService).saveEntity(any());
    //then
    completeApplication.completeLectureByIdAndEmail(id, email);

    //then
    verify(memberService, timeout(1)).getMemberByEmail(anyString());
    verify(lectureService, timeout(1)).getLectureById(anyLong());
    verify(listenService, timeout(1)).existCheckByMemberAndCourse(any(), any());
    verify(courseLectureService, timeout(1)).existCourseIdByMemberAndId(any(), anyLong());
    verify(courseLectureService, never()).getCourseLectureByMemberAndId(any(), anyLong());
    verify(courseLectureService, timeout(1)).saveEntity(any());
  }

  @Test
  @DisplayName("강의를 수강한 경우 티어가 갱신되고 MemberCourseLecturer값이 저장되는 로직 - 실패")
  void completeLectureByIdAndEmail_fail() {
    //given
    Member member = CommonHelper.createOriginMemberFormByNoId();
    Lecture lecture = CommonHelper.createLecture();
    Long id = 1L;
    String email = "wpekdl153@gmail.com";
    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    when(lectureService.getLectureById(anyLong()))
        .thenReturn(lecture);
    when(listenService.existCheckByMemberAndCourse(any(), any()))
        .thenReturn(false);
    //when
    SuperException result = assertThrows(
        ExceptionExistListening.class,
        () -> completeApplication.completeLectureByIdAndEmail(id, email));

    //then
    assertEquals(result.getMessage(), "수강 중이지 않는 강좌입니다.");
    verify(memberService, timeout(1)).getMemberByEmail(anyString());
    verify(lectureService, timeout(1)).getLectureById(anyLong());
    verify(listenService, timeout(1)).existCheckByMemberAndCourse(any(), any());
    verify(courseLectureService, never()).existCourseIdByMemberAndId(any(), anyLong());
    verify(courseLectureService, never()).getCourseLectureByMemberAndId(any(), anyLong());
    verify(courseLectureService, never()).saveEntity(any());
  }
}