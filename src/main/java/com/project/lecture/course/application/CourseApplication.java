package com.project.lecture.course.application;


import com.project.lecture.course.dto.CourseRequest.Create;
import com.project.lecture.course.dto.LectureDto;
import com.project.lecture.course.service.CourseService;
import com.project.lecture.course.service.LectureService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.user.service.MemberService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CourseApplication {

  private final CourseService courseService;
  private final MemberService memberService;
  private final LectureService lectureService;

  @Transactional
  public void createCourseAndLecture(Create request, String name) {
    Member member = memberService.getEmail(name);

    Course course = courseService.createCourse(request,member);

    lectureService.ListInsert(
        request.getLectures()
            .stream().map(dto -> LectureDto.toEntity(dto,course))
            .collect(Collectors.toList())
    );
  }
}
