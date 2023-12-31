package com.project.lecture.api.study.service;

import com.project.lecture.entity.Member;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.kind.ExceptionCompleteStudy;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import com.project.lecture.exception.kind.ExceptionNotValidUser;
import com.project.lecture.repository.StudyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Study study = getStudyById(id);

    if (!study.getMember().getEmail().equals(email)) {
      throw new ExceptionNotValidUser(study.getStudyTitle());
    }
    if (study.isStudyComplete()) {
      throw new ExceptionCompleteStudy();
    }

    study.completeStudy();
  }

  public Study getStudyById(Long id) {
    return studyRepository.findById(id)
        .orElseThrow(ExceptionNotFoundStudy::new);
  }

  public boolean existStudyByIdAndEmail(Long studyId, String email) {
    return studyRepository.existsByStudyIdAndMember_Email(studyId,email);
  }

  public void deleteStudy(Long studyId) {
    studyRepository.deleteById(studyId);
  }

  public Page<Study> getListByEmailAndPage(String email, Pageable pageable) {
    return studyRepository.findByMember_Email(email,pageable);
  }

  public List<Study> getStudiesByNoComplete(Member member, boolean completeFlag) {
    return studyRepository.findAllByMemberAndStudyComplete(member,completeFlag);
  }
}
