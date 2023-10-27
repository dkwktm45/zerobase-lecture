package com.project.lecture.course.application;

import static com.project.lecture.type.ResponseType.COURSE_SUCCESS;

import com.project.lecture.course.dto.CourseRequest.Create;
import com.project.lecture.course.service.CourseService;
import com.project.lecture.entity.Member;
import com.project.lecture.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CourseApplication {

  private final CourseService courseService;
  private final MemberService memberService;

  @Transactional
  public String createCourseAndLecture(Create request, String name) {
    Member member = memberService.getEmail(name);

    courseService.createCourse(request).setMember(member);

    return COURSE_SUCCESS.getDescription();
  }
}
