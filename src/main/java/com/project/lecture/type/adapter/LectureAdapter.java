package com.project.lecture.type.adapter;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.complete.service.CourseLectureService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.planner.dto.StudyTypeDto;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.exception.kind.ExceptionCompleteLecture;
import com.project.lecture.exception.kind.ExceptionExistListening;
import com.project.lecture.redis.LectureClient;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.TypeContent;
import com.project.lecture.type.TypeRequest.Create;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Component
@Transactional
public class LectureAdapter implements TypeAdapter {

  private final ListenService listenService;
  private final LectureService lectureService;
  private final CourseService courseService;
  private final LectureClient lectureClient;
  private final CourseLectureService courseLectureService;
  private static int plusTime;

  @Override
  public boolean existCheck(Create create, String email, Long memberId) {
    Lecture lecture = lectureService.getLectureById(create.getTypeId());
    return listenService.existCheck(lecture.getCourse().getCourseId(), memberId);
  }

  @Override
  public TypeContent getContent(Long id) {
    Lecture lecture = lectureService.getLectureById(id);
    return new TypeContent(lecture.getLectureName(), null);
  }

  @Override
  public void exceptionThrow() {
    throw new ExceptionExistListening();
  }

  @Override
  public void complete(Long id, Member member) {
    log.info("complete 수행");
    Course course = courseService.getCourseByLectureId(id);

    if (!listenService.existCheckByMemberAndCourse(member, course)) {
      throw new ExceptionExistListening();
    }
    plusTime = lectureClient.getLectureTime(course.getCourseId(), id);
    Optional<MemberCourseLecture> courseLectureOptional = courseLectureService
        .getCourseLectureByMemberAndId(member, course.getCourseId());

    courseLectureOptional.ifPresentOrElse(
        info -> {
          if (!info.getMemberLectureMap()
              .containsKey(id)) {

            info.getMemberLectureMap().put(id, LocalDate.now());
          } else {
            throw new ExceptionCompleteLecture();
          }
        }, () -> {
          HashMap<Long, LocalDate> memberLectureHashMap = new HashMap<>();
          memberLectureHashMap.put(id, LocalDate.now());

          courseLectureService.saveEntity(
              MemberCourseLecture.builder()
                  .member(member)
                  .courseId(course.getCourseId())
                  .memberLectureMap(memberLectureHashMap)
                  .build()
          );
        });

    courseLectureService.completePlusTierByEmailAndTime(member.getEmail(), plusTime);
    log.info("complete 마침");
  }

  @Override
  public StudyType getStudyType() {
    return StudyType.LECTURE;
  }

  @Override
  public List<StudyTypeDto> getTypeStudies(Member member ,boolean completeFlag) {
    return null;
  }
}
