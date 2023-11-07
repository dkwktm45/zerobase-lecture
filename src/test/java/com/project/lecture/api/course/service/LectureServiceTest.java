package com.project.lecture.api.course.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Lecture;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionNotFoundLecture;
import com.project.lecture.repository.LectureRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

  @Mock
  private LectureRepository lectureRepository;
  @InjectMocks
  private LectureService lectureService;

  @Test
  @DisplayName("강의 목록 저장 - 성공")
  void listInsert() {
    List<Lecture> args = CommonHelper.createLecturesForm();

    // given
    when(lectureRepository.saveAll(any()))
        .thenReturn(args);

    // when
    lectureService.ListInsert(args);

    // then
    verify(lectureRepository, timeout(1)).saveAll(any());
  }

  @Test
  @DisplayName("id를 통한 조회 - 성공")
  void getLectureById_success() {
    // given
    Lecture arg = CommonHelper.createLecture();
    Long id = 1L;
    when(lectureRepository.findById(anyLong()))
        .thenReturn(Optional.of(arg));

    // when
    lectureService.getLectureById(id);

    // then
    verify(lectureRepository, timeout(1)).findById(anyLong());
  }

  @Test
  @DisplayName("id를 통한 조회 - 실패[empty]")
  void getLectureById_fail() {
    // given
    Long id = 1L;
    when(lectureRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    // when
    SuperException result = assertThrows(
        ExceptionNotFoundLecture.class,
        () -> lectureService.getLectureById(id));

    // then
    assertEquals(result.getMessage(), "강의가 존재하지 않습니다.");
    verify(lectureRepository, timeout(1)).findById(anyLong());
  }
}