package com.project.lecture.Listen.application;

import com.project.lecture.Listen.service.ListenService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Listening;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.ExceptionExistCourse;
import com.project.lecture.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListenApplication {

  private final ListenService listenService;
  private final MemberService memberService;

  public void listenCourse(Long courseId, String email) {
    Member member = memberService.getMemberByEmail(email);
    Course course = Course.builder().courseId(courseId).build();
    Listening listening;

    if (!listenService.existCheck(courseId, member.getMemberId())) {
      listening = Listening.builder()
          .course(course)
          .member(member).build();
      listenService.saveListening(listening);
    } else {
      throw new ExceptionExistCourse();
    }
  }
}
