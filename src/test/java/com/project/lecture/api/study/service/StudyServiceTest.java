package com.project.lecture.api.study.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionCompleteStudy;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import com.project.lecture.exception.kind.ExceptionNotValidUser;
import com.project.lecture.repository.StudyRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

  @Mock
  private StudyRepository studyRepository;
  @InjectMocks
  private StudyService studyService;

  @Test
  @DisplayName("Study 정보 저장")
  void createStudy() {
    // given
    Study study = CommonHelper.createStudyByNoId();
    when(studyRepository.save(any()))
        .thenReturn(study);

    // when
    studyService.createStudy(study);

    // then
    verify(studyRepository, timeout(1)).save(any());
  }

  @Test
  @DisplayName("해당 study정보를 가져와 완료했다는 상태로 바꾸는 로직 - 성공")
  void completeStudyById_success() {
    // given
    long id = 1L;
    String email = "planner@gmail.com";
    Study study = CommonHelper.createStudyByNoId();

    when(studyRepository.findById(anyLong()))
        .thenReturn(Optional.of(study));

    // when
    studyService.completeStudyById(id, email);
    // then
    verify(studyRepository, timeout(1)).findById(anyLong());
  }

  @Test
  @DisplayName("해당 study정보를 가져와 완료했다는 상태로 바꾸는 로직 - 실패[완료 상태가 true]")
  void completeStudyById_fail_complete_true() {
    // given
    long id = 1L;
    String email = "planner@gmail.com";
    Study study = CommonHelper.createStudyCompleteTrue();

    when(studyRepository.findById(anyLong()))
        .thenReturn(Optional.of(study));

    // when
    SuperException result = assertThrows(ExceptionCompleteStudy.class,
        () -> studyService.completeStudyById(id, email));
    // then
    assertEquals(result.getMessage(), "이미 완료한 개인학습입니다.");
    verify(studyRepository, timeout(1)).findById(anyLong());
  }

  @Test
  @DisplayName("해당 study정보를 가져와 완료했다는 상태로 바꾸는 로직 - 실패[유저정보 불일치]")
  void completeStudyById_fail_valid_user() {
    // given
    long id = 1L;
    String email = "planner66@gmail.com";
    Study study = CommonHelper.createStudyCompleteTrue();

    when(studyRepository.findById(anyLong()))
        .thenReturn(Optional.of(study));

    // when
    SuperException result = assertThrows(ExceptionNotValidUser.class,
        () -> studyService.completeStudyById(id, email));
    // then
    assertEquals(result.getMessage(), "일치하지 않는 회원입니다.");
    verify(studyRepository, timeout(1)).findById(anyLong());
  }

  @Test
  @DisplayName("해당 study정보를 가져와 완료했다는 상태로 바꾸는 로직 - 실패[존재하지 않는 개인학습]")
  void completeStudyById_fail_not_found() {
    // given
    when(studyRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    // when
    SuperException result = assertThrows(ExceptionNotFoundStudy.class,
        () -> studyService.completeStudyById(1L, anyString()));

    // then
    assertEquals(result.getMessage(), "존재하지 않는 개인학습입니다.");
    verify(studyRepository, timeout(1)).findById(anyLong());
  }
  @Test
  @DisplayName("id값을 통해서 Study값을 가져오는 로직 - 성공")
  void getStudyById_success(){
    //given
    Study study = CommonHelper.createStudyByNoId();
    Long id = 1L;

    when(studyRepository.findById(anyLong()))
        .thenReturn(Optional.of(study));
    //when
    Study result = studyService.getStudyById(id);

    //then
    assertEquals(study.getStudyTitle(),result.getStudyTitle());
    assertEquals(study.getStudyContent(),result.getStudyContent());
  }
  @Test
  @DisplayName("id값을 통해서 Study값을 가져오는 로직 - 실패")
  void getStudyById_fail(){
    //given
    Long id = 1L;

    when(studyRepository.findById(anyLong()))
        .thenReturn(Optional.empty());
    //when
    SuperException result = assertThrows(ExceptionNotFoundStudy.class,
        () -> studyService.getStudyById(id));

    //then
    assertEquals(result.getMessage(),"존재하지 않는 개인학습입니다.");
  }
  @Test
  @DisplayName("id에 해당하는 값이 존재하는지 확인 - true")
  void existStudy_true(){
    //given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(studyRepository.existsByStudyIdAndMember_Email(id,email))
        .thenReturn(true);
    //when
    boolean result = studyService.existStudyByIdAndEmail(id,email);

    //then
    assertTrue(result);
  }
  @Test
  @DisplayName("id에 해당하는 값이 존재하는지 확인 - true")
  void existStudy_false(){
    //given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(studyRepository.existsByStudyIdAndMember_Email(id,email))
        .thenReturn(false);
    //when
    boolean result = studyService.existStudyByIdAndEmail(id,email);

    //then
    assertFalse(result);
  }
  @Test
  @DisplayName("id를 통해 값을 삭제한다.")
  void deleteStudy(){
    //given
    Long id = 1L;
    doNothing().when(studyRepository).deleteById(id);
    //when
    studyService.deleteStudy(id);
    //then
    verify(studyRepository,timeout(1)).deleteById(id);
  }
}