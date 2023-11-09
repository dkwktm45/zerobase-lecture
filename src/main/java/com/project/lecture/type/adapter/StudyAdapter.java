package com.project.lecture.type.adapter;

import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.TypeContent;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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
}
