package com.project.lecture.api.complete.application;

import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.adapter.TypeAdapter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteApplication {

  private final MemberService memberService;
  private final Map<StudyType, TypeAdapter> typeAdapterMap;

  @Transactional
  public void completeCourseByIdAndEmail(Long courseId, String email) {
    Member member = memberService.getMemberByEmail(email);
    TypeAdapter typeAdapter = typeAdapterMap.get(StudyType.COURSE);
    typeAdapter.complete(courseId, member);
  }



  @Transactional
  public void completeLectureByIdAndEmail(Long lectureId, String email) {
    Member member = memberService.getMemberByEmail(email);
    TypeAdapter typeAdapter = typeAdapterMap.get(StudyType.LECTURE);
    typeAdapter.complete(lectureId, member);
  }
}
