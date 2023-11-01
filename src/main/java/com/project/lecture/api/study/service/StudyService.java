package com.project.lecture.api.study.service;

import com.project.lecture.entity.Study;
import com.project.lecture.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

  private final StudyRepository studyRepository;

  public void createStudy(Study study) {
    studyRepository.save(study);
  }
}
