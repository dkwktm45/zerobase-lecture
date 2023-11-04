package com.project.lecture.api.course.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Lecture;
import com.project.lecture.repository.LectureRepository;
import java.util.List;
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
}