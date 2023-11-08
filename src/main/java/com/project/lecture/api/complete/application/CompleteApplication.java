package com.project.lecture.api.complete.application;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.complete.dto.AddMemberLecture;
import com.project.lecture.api.complete.service.CourseLectureService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.entity.json.MemberLecture;
import com.project.lecture.exception.kind.ExceptionExistListening;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteApplication {


  private final CourseService courseService;
  private final LectureService lectureService;
  private final ListenService listenService;
  private final MemberService memberService;
  private final CourseLectureService courseLectureService;

  @Transactional
  public void completeCourseByIdAndEmail(Long courseId, String email) {
    Member member = memberService.getMemberByEmail(email);
    Course course = courseService.getCourseById(courseId);

    if (!listenService.existCheckByMemberAndCourse(member, course)) {
      throw new ExceptionExistListening();
    }
    HashMap<Long, MemberLecture> memberLectures = new HashMap<>();
    List<Lecture> lectures = course.getLectures();
    int totalTime = 0;

    if (courseLectureService.existCourseIdByMemberAndId(member, course.getCourseId())) {
      MemberCourseLecture memberCourseLecture = courseLectureService
          .getCourseLectureByMemberAndId(member, course.getCourseId());

      AddMemberLecture addMemberLecture = updateMemberCourseLecture(lectures,
          memberCourseLecture.getMemberLectures());

      totalTime = addMemberLecture.getTime();
      memberLectures = addMemberLecture.getMemberLectures();

      memberCourseLecture.updateMemberLecture(memberLectures);
    } else {
      for (Lecture lecture : lectures) {
        memberLectures.put(lecture.getLectureId(),
            new MemberLecture(lecture.getLectureTime(), LocalDateTime.now()));
        totalTime += lecture.getLectureTime();
      }

      courseLectureService.saveEntity(
          MemberCourseLecture.builder()
              .member(member)
              .courseId(courseId)
              .memberLectures(memberLectures)
              .build()
      );
    }

    courseLectureService.completePlusTierByEmailAndTime(member.getEmail(), totalTime);

  }

  public AddMemberLecture updateMemberCourseLecture(
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
    Lecture lecture = lectureService.getLectureById(lectureId);
    Course course = lecture.getCourse();

    if (!listenService.existCheckByMemberAndCourse(member, course)) {
      throw new ExceptionExistListening();
    }
    int plusTime = 0;
    if (courseLectureService.existCourseIdByMemberAndId(member, course.getCourseId())) {
      MemberCourseLecture memberCourseLecture = courseLectureService
          .getCourseLectureByMemberAndId(member, course.getCourseId());

      if (!memberCourseLecture.getMemberLectures()
          .containsKey(lectureId)) {

        memberCourseLecture.getMemberLectures()
            .put(lectureId, new MemberLecture(lecture.getLectureTime(), LocalDateTime.now()));
      }
    } else {
      HashMap<Long, MemberLecture> memberLectureHashMap = new HashMap<>();
      memberLectureHashMap.put(lectureId,
          new MemberLecture(lecture.getLectureTime(), LocalDateTime.now()));

      courseLectureService.saveEntity(
          MemberCourseLecture.builder()
              .member(member)
              .courseId(course.getCourseId())
              .memberLectures(memberLectureHashMap)
              .build()
      );
    }
    plusTime += lecture.getLectureTime();
    courseLectureService.completePlusTierByEmailAndTime(member.getEmail(), plusTime);
  }
}
