package com.project.lecture.api.study.application;

import com.project.lecture.api.study.dto.StudyRequest.Create;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyApplication {

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
}
