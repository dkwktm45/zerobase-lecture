package com.project.lecture.api.study.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
    Study study = CommonHelper.createStudy();
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
    Study study = CommonHelper.createStudy();

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
}