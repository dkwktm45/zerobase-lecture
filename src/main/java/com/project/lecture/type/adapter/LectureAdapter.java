package com.project.lecture.type.adapter;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.complete.service.CourseLectureService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.entity.json.MemberLecture;
import com.project.lecture.exception.kind.ExceptionCompleteLecture;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.TypeContent;
import com.project.lecture.entity.Lecture;
import com.project.lecture.exception.kind.ExceptionExistListening;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class LectureAdapter implements TypeAdapter {

  private final ListenService listenService;
  private final LectureService lectureService;
  private final CourseLectureService courseLectureService;

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
    Lecture lecture = lectureService.getLectureById(id);
    Course course = lecture.getCourse();

    if (!listenService.existCheckByMemberAndCourse(member, course)) {
      throw new ExceptionExistListening();
    }
    int plusTime = 0;
    Optional<MemberCourseLecture> courseLectureOptional = courseLectureService
        .getCourseLectureByMemberAndId(member, course.getCourseId());

    courseLectureOptional.ifPresentOrElse(
        info -> {
          if (!info.getMemberLectureMap()
              .containsKey(id)) {

            info.getMemberLectureMap()
                .put(id, new MemberLecture(lecture.getLectureTime(), LocalDateTime.now()));
          } else {
            throw new ExceptionCompleteLecture();
          }
        }, () -> {
          HashMap<Long, MemberLecture> memberLectureHashMap = new HashMap<>();
          memberLectureHashMap.put(id,
              new MemberLecture(lecture.getLectureTime(), LocalDateTime.now()));

          courseLectureService.saveEntity(
              MemberCourseLecture.builder()
                  .member(member)
                  .courseId(course.getCourseId())
                  .memberLectureMap(memberLectureHashMap)
                  .build()
          );
        });

    plusTime += lecture.getLectureTime();
    courseLectureService.completePlusTierByEmailAndTime(member.getEmail(), plusTime);
    log.info("complete 마침");
  }
}
