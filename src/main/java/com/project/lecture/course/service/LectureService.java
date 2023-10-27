package com.project.lecture.course.service;

import com.project.lecture.entity.Lecture;
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
  public void ListInsert(List<Lecture> lectureList) {
    lectureRepository.saveAll(lectureList);
  }
}
