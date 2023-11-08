package com.project.lecture.api.complete.application;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.complete.service.CourseLectureService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.entity.json.MemberLecture;
import com.project.lecture.exception.kind.ExceptionCompleteCourse;
import com.project.lecture.exception.kind.ExceptionExistListening;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompleteApplication {


  private final CourseService courseService;
  private final ListenService listenService;
  private final MemberService memberService;
  private final CourseLectureService courseLectureService;

  public void completeCourseByIdAndEmail(Long courseId, String email) {
    Member member = memberService.getMemberByEmail(email);
    Course course = courseService.getCourseById(courseId);

    if (!listenService.existCheckByMemberAndCourse(member, course)) {
      throw new ExceptionExistListening();
    }

    if (courseLectureService.existCourseIdByMemberAndId(member, course.getCourseId())) {
      throw new ExceptionCompleteCourse();
    }

    List<MemberLecture> memberLectures = new ArrayList<>();
    List<Lecture> lectures = course.getLectures();
    int totalTime = 0;
    for (Lecture lecture : lectures) {
      memberLectures.add(new MemberLecture(
          lecture.getLectureId()
          , lecture.getLectureTime(),
          LocalDateTime.now()));
      totalTime += lecture.getLectureTime();
    }

    courseLectureService.completePlusTierByEmailAndTime(member.getEmail(), totalTime);

    courseLectureService.saveEntity(
        MemberCourseLecture.builder()
            .member(member).memberLectures(memberLectures)
            .build()
    );
  }
}
