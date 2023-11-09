package com.project.lecture.api.course.application;


import com.project.lecture.api.course.dto.CourseDto;
import com.project.lecture.api.course.dto.CourseRequest.Create;
import com.project.lecture.api.course.dto.CreateLecture;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.api.user.service.MemberService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminCourseApplication {

  private final CourseService courseService;
  private final MemberService memberService;
  private final LectureService lectureService;

  @Transactional
  public void createCourseAndLecture(Create request, String name) {
    Member member = memberService.getMemberByEmail(name);

    Course course = courseService.createCourse(request, member);

    lectureService.ListInsert(
        request.getLectures()
            .stream().map(dto -> CreateLecture.toEntity(dto, course))
            .collect(Collectors.toList())
    );
  }

  public Page<CourseDto> getCourseList(String email, Pageable pageable) {
    Page<Course> pageCourse = courseService.getListByEmailAndPage(email, pageable);

    return CourseDto.toDotList(pageCourse);
  }

}
