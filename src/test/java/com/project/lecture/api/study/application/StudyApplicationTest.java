package com.project.lecture.api.study.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.study.dto.StudyDto;
import com.project.lecture.api.study.dto.StudyRequest.Change;
import com.project.lecture.api.study.dto.StudyRequest.Create;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import com.project.lecture.exception.kind.ExceptionNotValidUser;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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
    Study study = CommonHelper.createStudyByNoId();
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
    String email = "planner@gmail.com";
    when(studyService.existStudyByIdAndEmail(anyLong(),anyString()))
        .thenReturn(true);
    doNothing().when(studyService).deleteStudy(anyLong());
    when(plannerService.existByStudyIdAndType(anyLong(),any()))
        .thenReturn(true);
    doNothing().when(plannerService).deletePlanner(anyLong(), any());
    // when
    studyApplication.deleteStudyById(id, email);

    // then
    verify(studyService, timeout(1)).existStudyByIdAndEmail(anyLong(),anyString());
    verify(studyService, timeout(1)).deleteStudy(anyLong());
    verify(plannerService, timeout(1)).existByStudyIdAndType(anyLong(),any());
    verify(plannerService, timeout(1)).deletePlanner(anyLong(), any());
  }

  @Test
  @DisplayName("study 삭제 및 planner에 존재 한다면 삭제하는 로직 - 성공[planner 존재 O]")
  void deleteStudyById_success_not_exist_planner() {
    // given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(studyService.existStudyByIdAndEmail(anyLong(),anyString()))
        .thenReturn(true);
    doNothing().when(studyService).deleteStudy(anyLong());
    when(plannerService.existByStudyIdAndType(anyLong(),any()))
        .thenReturn(false);
    // when
    studyApplication.deleteStudyById(id, email);

    // then
    verify(studyService, timeout(1)).existStudyByIdAndEmail(anyLong(),anyString());
    verify(studyService, timeout(1)).deleteStudy(anyLong());
    verify(plannerService, timeout(1)).existByStudyIdAndType(anyLong(),any());
    verify(plannerService, never()).deletePlanner(anyLong(), any());
  }

  @Test
  @DisplayName("study 삭제 및 planner에 존재 한다면 삭제하는 로직 - 실패")
  void deleteStudyById_fail() {
    // given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(studyService.existStudyByIdAndEmail(anyLong(),anyString()))
        .thenReturn(false);
    // when
    SuperException result = Assert.assertThrows(ExceptionNotFoundStudy.class,
        () -> studyApplication.deleteStudyById(id, email));

    // then
    assertEquals(result.getMessage(), "존재하지 않는 개인학습입니다.");
    verify(studyService, timeout(1)).existStudyByIdAndEmail(anyLong(),anyString());
    verify(studyService, never()).deleteStudy(anyLong());
    verify(plannerService, never()).existByStudyIdAndType(anyLong(),any());
    verify(plannerService, never()).deletePlanner(anyLong(), any());
  }

  @Test
  @DisplayName("email을 통한 Study 여러건 조회")
  void getStudiesByEmail() {
    //given
    List<Study> studies = CommonHelper.createStudyList();
    Page<Study> studyPage = new PageImpl<>(studies);
    when(studyService.getListByEmailAndPage(any(),any()))
        .thenReturn(studyPage);

    //when
    Page<StudyDto> studyDtoPage = studyApplication.getStudiesByEmail(any(),any());

    //then
    List<StudyDto> result = studyDtoPage.getContent();
    for (int i = 0; i < result.size(); i++) {
      assertEquals(result.get(i).getId(), studies.get(i).getStudyId());
      assertEquals(result.get(i).getStudyContent(), studies.get(i).getStudyContent());
      assertEquals(result.get(i).getStudyTitle(), studies.get(i).getStudyTitle());
      assertEquals(result.get(i).isStudyComplete(), studies.get(i).isStudyComplete());
    }
    verify(studyService, timeout(1)).getListByEmailAndPage(any(),any());
  }

  @Test
  @DisplayName("Id를 통한 Study 단건 조회 - 성공")
  void getStudyByEmail_success(){
    //given
    Long id = 1L;
    String email = "planner@gmail.com";
    Study study = CommonHelper.createStudy();
    when(studyService.getStudyById(anyLong()))
        .thenReturn(study);

    //when
    StudyDto result = studyApplication.getStudyByEmail(id, email);

    //then
    assertEquals(result.getStudyTitle(),study.getStudyTitle());
    assertEquals(result.getId(),study.getStudyId());
    assertEquals(result.getStudyContent(),study.getStudyContent());
  }

  @Test
  @DisplayName("Id를 통한 Study 단건 조회 - 실패")
  void getStudyByEmail_false(){
    //given
    Long id = 1L;
    String email = "12345@gmail.com";
    Study study = CommonHelper.createStudy();
    when(studyService.getStudyById(anyLong()))
        .thenReturn(study);

    //when
    SuperException result = assertThrows(ExceptionNotValidUser.class,
        () -> studyApplication.getStudyByEmail(id, email)) ;

    //then
    assertEquals(result.getMessage(),"일치하지 않는 회원입니다.");
  }

}
