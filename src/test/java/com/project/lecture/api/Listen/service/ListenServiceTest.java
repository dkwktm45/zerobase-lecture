package com.project.lecture.api.Listen.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Listening;
import com.project.lecture.repository.ListeningRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListenServiceTest {

  @Mock
  private ListeningRepository listeningRepository;
  @InjectMocks
  private ListenService listenService;

  @Test
  @DisplayName("값이 존재하는지 체크 - true")
  void existCheck_true() {
    Long courseId = 1L;
    Long memberId = 1L;

    // given
    when(listeningRepository.existsByMember_MemberIdAndCourse_CourseId(any(),any()))
        .thenReturn(true);

    // when
    boolean result = listenService.existCheck(courseId, memberId);

    //then
    assertTrue(result);
  }
  @Test
  @DisplayName("값이 존재하는지 체크 - true")
  void existCheck_false() {
    Long courseId = 1L;
    Long memberId = 1L;

    // given
    when(listeningRepository.existsByMember_MemberIdAndCourse_CourseId(any(),any()))
        .thenReturn(false);

    // when
    boolean result = listenService.existCheck(courseId, memberId);

    //then
    assertFalse(result);
  }
  @Test
  @DisplayName("Listening 저장")
  void saveListening() {
    // given
    Listening listening = CommonHelper.createListingForm();
    when(listeningRepository.save(any()))
        .thenReturn(listening);

    // when
    listenService.saveListening(listening);

    //then
    verify(listeningRepository, timeout(1)).save(any());
  }

  @Test
  @DisplayName("Listening 삭제")
  void deleteListing() {
    // given
    Long courseId = 1L;
    Long memberId = 1L;
    doNothing().when(listeningRepository)
        .deleteByMemberIdAndCourseId(anyLong(), anyLong());
    // when
    listenService.deleteListing(courseId,memberId);

    //then
    verify(listeningRepository,timeout(1))
        .deleteByMemberIdAndCourseId(anyLong(), anyLong());
  }
}