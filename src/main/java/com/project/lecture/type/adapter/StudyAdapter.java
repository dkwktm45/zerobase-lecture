package com.project.lecture.type.adapter;

import com.project.lecture.entity.Member;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.TypeContent;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudyAdapter implements TypeAdapter {

  private final StudyService studyService;

  @Override
  public boolean existCheck(Create create, String email, Long id) {
    return studyService.existStudyByIdAndEmail(create.getTypeId(), email);
  }

  @Override
  public TypeContent getContent(Long id) {
    Study study = studyService.getStudyById(id);
    return new TypeContent(study.getStudyTitle(), study.getStudyContent());
  }
  @Override
  public void exceptionThrow(){
    throw new ExceptionNotFoundStudy();
  }

  @Override
  public void complete(Long id, Member member) {
    log.info("complete 수행");
    Study study = studyService.getStudyById(id);
    study.completeStudy();
    log.info("complete 마침");
  }
}
