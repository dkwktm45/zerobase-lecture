package com.project.lecture.type.adapter;

import com.project.lecture.api.planner.dto.StudyTypeDto;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Planner;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.TypeContent;
import com.project.lecture.type.TypeRequest.Create;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Component
public class StudyAdapter implements TypeAdapter {

  private final StudyService studyService;
  private final PlannerService plannerService;

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

  @Override
  public StudyType getStudyType() {
    return StudyType.STUDY;
  }

  @Override
  public List<StudyTypeDto> getTypeStudies(Member member,boolean completeFlag) {
    List<Study> studies = studyService.getStudiesByNoComplete(member,completeFlag);
    List<Planner> planners = plannerService
        .getPlannersByNoComplete(member, getStudyType(), completeFlag);
    return studies.stream()
        .filter(study ->
            planners.stream()
                .noneMatch(planner -> study.getStudyId().equals(planner.getPlannerTypeId()))
        ).map(StudyTypeDto::toStudyDto).collect(Collectors.toList());
  }


}
