package com.project.lecture.type.adapter;


import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.complete.dto.AddMemberLecture;
import com.project.lecture.api.complete.service.CourseLectureService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.planner.dto.StudyTypeDto;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.entity.Planner;
import com.project.lecture.exception.kind.ExceptionCompleteCourse;
import com.project.lecture.exception.kind.ExceptionExistListening;
import com.project.lecture.redis.LectureClient;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.TypeContent;
import com.project.lecture.type.TypeRequest.Create;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Component
public class CourseAdapter implements TypeAdapter {

  private final ListenService listenService;
  private final CourseService courseService;
  private final CourseLectureService courseLectureService;
  private final LectureClient lectureClient;
  private static int totalTime;

  @Override
  public boolean existCheck(Create create, String email, Long memberId) {
    return listenService.existCheck(create.getTypeId(), memberId);
  }

  @Override
  public TypeContent getContent(Long id) {
    Course course = courseService.getCourseById(id);
    return new TypeContent(course.getCourseName(), course.getCourseContent());
  }

  @Override
  public void exceptionThrow() {
    throw new ExceptionExistListening();
  }

  @Override
  public void complete(Long id, Member member) {
    log.info("complete 수행");
    Course course = courseService.getCourseById(id);
    List<Integer> lectureKeys = lectureClient.getKeys(course.getCourseId());
    Optional<MemberCourseLecture> memberCourseLecture = courseLectureService
        .getCourseLectureByMemberAndId(member, course.getCourseId());
    totalTime = 0;

    memberCourseLecture.ifPresentOrElse(
        info -> {
          HashMap<Long, LocalDate> memberLectures;

          AddMemberLecture addMemberLecture = updateMemberCourseLecture
              (
                  id,
                  lectureKeys,
                  info.getMemberLectureMap()
              );

          totalTime = addMemberLecture.getTime();
          memberLectures = addMemberLecture.getMemberLectures();

          info.updateMemberLecture(memberLectures);
        }, () -> {
          HashMap<Long, LocalDate> memberLectures = new HashMap<>();

          for (long keyId : lectureKeys) {
            memberLectures.put(keyId, LocalDate.now());
          }
          totalTime = course.getTotalTime();
          courseLectureService.saveEntity(
              MemberCourseLecture.builder()
                  .member(member)
                  .courseId(course.getCourseId())
                  .memberLectureMap(memberLectures)
                  .build()
          );
        }
    );

    courseLectureService.completePlusTierByEmailAndTime(member.getEmail(), totalTime);
    log.info("complete 마침");
  }

  @Override
  public StudyType getStudyType() {
    return StudyType.COURSE;
  }

  @Override
  public List<StudyTypeDto> getTypeStudies(Member member, boolean completeFlag) {
    List<Course> courses = courseService.getListByMember(member);
    List<Planner> planners = member.getPlanners();

    return courses.stream()
        .filter(course -> planners.stream()
            .noneMatch(planner -> course.getCourseId().equals(planner.getPlannerTypeId())))
        .map(StudyTypeDto::toCourseDto)
        .collect(Collectors.toList());
  }

  private AddMemberLecture updateMemberCourseLecture(
      Long courseId, List<Integer> lectures,
      HashMap<Long, LocalDate> memberLectures
  ) {

    int time = 0;
    for (long keyId : lectures) {
      if (!memberLectures.containsKey(keyId)) {
        int currTime = lectureClient.getLectureTime(courseId, keyId);
        memberLectures.put(keyId, LocalDate.now());
        time += currTime;
      }
    }
    if (time == 0) {
      throw new ExceptionCompleteCourse();
    }

    return new AddMemberLecture(time, memberLectures);
  }
}
