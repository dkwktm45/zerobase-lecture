package com.project.lecture.api.study.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.study.dto.StudyRequest.Create;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyApplicationTest {

  @Mock
  private MemberService memberService;
  @Mock
  private StudyService studyService;
  @InjectMocks
  private StudyApplication studyApplication;
  @Test
  @DisplayName("request요청을 study 엔티티로 변환해서 저장하는 로직")
  void createStudyByRequest() {
    // given
    Member member = CommonHelper.createMemberForm();
    Create create = CommonHelper.studyRequestCreate();
    when(memberService.getMemberByEmail(any()))
        .thenReturn(member);
    doNothing().when(studyService).createStudy(any());

    // when
    studyApplication.createStudyByRequest(create,member.getEmail());

    // then
    verify(memberService, timeout(1)).getMemberByEmail(any());
    verify(studyService, timeout(1)).createStudy(any());
  }
}