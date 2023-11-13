package com.project.lecture.api.complete.application;

import com.project.lecture.api.complete.dto.AddMemberLecture;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.json.MemberLecture;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.adapter.TypeAdapter;
import java.util.HashMap;
import java.util.List;
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

  public static AddMemberLecture updateMemberCourseLecture(
      List<Lecture> lectures,
      HashMap<Long, MemberLecture> memberLectures
  ) {

    int time = 0;
    for (Lecture lecture : lectures) {
      if (!memberLectures.containsKey(lecture.getLectureId())) {
        memberLectures.put(lecture.getLectureId(), new MemberLecture());
        time += lecture.getLectureTime();
      }
    }

    return new AddMemberLecture(time, memberLectures);
  }

  @Transactional
  public void completeLectureByIdAndEmail(Long lectureId, String email) {
    Member member = memberService.getMemberByEmail(email);
    TypeAdapter typeAdapter = typeAdapterMap.get(StudyType.LECTURE);
    typeAdapter.complete(lectureId, member);
  }
}
