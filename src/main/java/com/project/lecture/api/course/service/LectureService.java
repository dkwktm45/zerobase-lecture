package com.project.lecture.api.course.service;

import com.project.lecture.entity.Lecture;
import com.project.lecture.exception.kind.ExceptionNotFoundLecture;
import com.project.lecture.repository.LectureRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LectureService {

  private final LectureRepository lectureRepository;

  @Transactional
  public List<Lecture> ListInsert(List<Lecture> lectureList) {
    return lectureRepository.saveAll(lectureList);
  }

  public Lecture getLectureById(Long id) {
    return lectureRepository.findById(id)
        .orElseThrow(ExceptionNotFoundLecture::new);
  }
}
