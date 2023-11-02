package com.project.lecture.api.study.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.study.dto.StudyRequest.Change;
import com.project.lecture.api.study.dto.StudyRequest.Create;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyApplicationTest {

  @Mock
  private MemberService memberService;
  @Mock
  private PlannerService plannerService;
  @Mock
  private StudyService studyService;
  @InjectMocks
  private StudyApplication studyApplication;

  @Test
  @DisplayName("request요청을 study 엔티티로 변환해서 저장하는 로직")
  void createStudyByRequest() {
    // given
    Member member = CommonHelper.createMemberForm();
    Create create = CommonHelper.studyRequestCreate();
    when(memberService.getMemberByEmail(any()))
        .thenReturn(member);
    doNothing().when(studyService).createStudy(any());

    // when
    studyApplication.createStudyByRequest(create, member.getEmail());

    // then
    verify(memberService, timeout(1)).getMemberByEmail(any());
    verify(studyService, timeout(1)).createStudy(any());
  }

  @Test
  @DisplayName("Study값을 수정하는 로직")
  void changeStudyByRequest() {
    // given
    Change request = CommonHelper.changeStudyForm();
    Study study = CommonHelper.createStudy();
    when(studyService.getStudyById(anyLong()))
        .thenReturn(study);

    // when
    studyApplication.changeStudyByRequest(request);

    // then
    verify(studyService, timeout(1)).getStudyById(anyLong());
  }

  @Test
  @DisplayName("study 삭제 및 planner에 존재 한다면 삭제하는 로직 - 성공[planner 존재 X]")
  void deleteStudyById_success_exist_planner() {
    // given
    Long id = 1L;

    when(studyService.existStudy(anyLong()))
        .thenReturn(true);
    doNothing().when(studyService).deleteStudy(anyLong());
    when(plannerService.existStudyId(anyLong()))
        .thenReturn(true);
    doNothing().when(plannerService).deletePlanner(anyLong(), any());
    // when
    studyApplication.deleteStudyById(id);

    // then
    verify(studyService, timeout(1)).existStudy(anyLong());
    verify(studyService, timeout(1)).deleteStudy(anyLong());
    verify(plannerService, timeout(1)).existStudyId(anyLong());
    verify(plannerService, timeout(1)).deletePlanner(anyLong(), any());
  }
  @Test
  @DisplayName("study 삭제 및 planner에 존재 한다면 삭제하는 로직 - 성공[planner 존재 O]")
  void deleteStudyById_success_not_exist_planner() {
    // given
    Long id = 1L;

    when(studyService.existStudy(anyLong()))
        .thenReturn(true);
    doNothing().when(studyService).deleteStudy(anyLong());
    when(plannerService.existStudyId(anyLong()))
        .thenReturn(false);
    // when
    studyApplication.deleteStudyById(id);

    // then
    verify(studyService, timeout(1)).existStudy(anyLong());
    verify(studyService, timeout(1)).deleteStudy(anyLong());
    verify(plannerService, timeout(1)).existStudyId(anyLong());
    verify(plannerService, never()).deletePlanner(anyLong(), any());
  }

  @Test
  @DisplayName("study 삭제 및 planner에 존재 한다면 삭제하는 로직 - 실패")
  void deleteStudyById_fail() {
    // given
    Long id = 1L;

    when(studyService.existStudy(anyLong()))
        .thenReturn(false);
    // when
    SuperException result = Assert.assertThrows(ExceptionNotFoundStudy.class,
        () -> studyApplication.deleteStudyById(id));

    // then
    assertEquals(result.getMessage(), "존재하지 않는 개인학습입니다.");
    verify(studyService, timeout(1)).existStudy(anyLong());
    verify(studyService, never()).deleteStudy(anyLong());
    verify(plannerService, never()).existStudyId(anyLong());
    verify(plannerService, never()).deletePlanner(anyLong(), any());
  }
}
