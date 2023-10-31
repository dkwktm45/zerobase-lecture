package com.project.lecture.Listen.application;

import com.project.lecture.Listen.service.ListenService;
import com.project.lecture.course.service.CourseService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Listening;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Planner;
import com.project.lecture.exception.kind.ExceptionExistCourse;
import com.project.lecture.exception.kind.ExceptionNotFoundCourse;
import com.project.lecture.planner.service.PlannerService;
import com.project.lecture.user.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ListenApplication {

  private final ListenService listenService;
  private final MemberService memberService;
  private final PlannerService plannerService;
  private final CourseService courseService;

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

  // todo 더 나은 방안이 있는 고민하자!
  public void deleteListenCourse(Long courseId, String email) {
    log.info("deleteListenCourse() 진입");
    Member member = memberService.getMemberByEmail(email);
    if (listenService.existCheck(courseId, member.getMemberId())) {
      listenService.deleteListing(courseId, member.getMemberId());
    } else {
      throw new ExceptionNotFoundCourse();
    }

    log.info("Planner에 강좌가 존재 하는지 확인");

    List<Planner> planners = member.getPlanners();

    if (planners.isEmpty()) {
      return;
    }
    plannerService.existCourseByPlanner(courseId, planners);

    List<Lecture> lectures = courseService.getCourseById(courseId).getLectures();
    log.info("Planner에 강의가 존재 하는지 확인");

    plannerService.existLectureByPlanner(planners, lectures);
  }
}
