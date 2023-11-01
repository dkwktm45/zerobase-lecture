package com.project.lecture.api.study.service;

import com.project.lecture.entity.Study;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import com.project.lecture.exception.kind.ExceptionNotValidUser;
import com.project.lecture.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

  private final StudyRepository studyRepository;

  public void createStudy(Study study) {
    studyRepository.save(study);
  }

  public void completeStudyById(Long id, String email) {
    Study study = studyRepository.findById(id)
        .orElseThrow(ExceptionNotFoundStudy::new);

    if (!study.getMember().getEmail().equals(email)) {
      throw new ExceptionNotValidUser(study.getStudyTitle());
    }

    study.completeStudy();
  }
}
