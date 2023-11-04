package com.project.lecture.api.study.application;

import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.study.dto.StudyDto;
import com.project.lecture.api.study.dto.StudyRequest.Change;
import com.project.lecture.api.study.dto.StudyRequest.Create;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import com.project.lecture.exception.kind.ExceptionNotValidUser;
import com.project.lecture.type.StudyType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyApplication {

  private final PlannerService plannerService;
  private final MemberService memberService;
  private final StudyService studyService;
  public void createStudyByRequest(Create request, String email) {
    Member member = memberService.getMemberByEmail(email);

    studyService.createStudy(
        Study.builder()
            .studyContent(request.getStudyContent())
            .studyTitle(request.getStudyTitle())
            .member(member).build()
    );
  }

  @Transactional
  public void changeStudyByRequest(Change request) {
    Study study = studyService.getStudyById(request.getId());

    study.changeComplete(request);
  }


  public void deleteStudyById(Long studyId, String email) {
    if (!studyService.existStudyByIdAndEmail(studyId,email)) {
      throw new ExceptionNotFoundStudy();
    }

    studyService.deleteStudy(studyId);

    if (plannerService.existStudyId(studyId)) {
      plannerService.deletePlanner(studyId, StudyType.STUDY);
    }

  }

  public Page<StudyDto> getStudiesByEmail(String email, Pageable pageable) {
    Page<Study> studies = studyService.getListByEmailAndPage(email,pageable);
    return StudyDto.toDotList(studies);
  }

  public StudyDto getStudyByEmail(Long id, String email) {
    Study study = studyService.getStudyById(id);

    if (!study.getMember().getEmail().equals(email)) {
      throw new ExceptionNotValidUser();
    }

    return new StudyDto(study);
  }
}
