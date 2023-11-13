package com.project.lecture.type.adapter;

import static com.project.lecture.api.complete.application.CompleteApplication.updateMemberCourseLecture;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.complete.dto.AddMemberLecture;
import com.project.lecture.api.complete.service.CourseLectureService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.entity.json.MemberLecture;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.TypeContent;
import com.project.lecture.entity.Course;
import com.project.lecture.exception.kind.ExceptionExistListening;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseAdapter implements TypeAdapter {
  private final ListenService listenService;
  private final CourseService courseService;
  private final CourseLectureService courseLectureService;
  private static int totalTime;
  @Override
  public boolean existCheck(Create create, String email, Long memberId) {
    return listenService.existCheck(create.getTypeId(), memberId);
  }

  @Override
  public TypeContent getContent(Long id) {
    Course course = courseService.getCourseById(id);
    return new TypeContent(course.getCourseName(),course.getCourseContent());
  }
  @Override
  public void exceptionThrow(){
    throw new ExceptionExistListening();
  }

  @Override
  public void complete(Long id, Member member) {
    log.info("complete 수행");
    Course course = courseService.getCourseById(id);
    List<Lecture> lectures = course.getLectures();
    Optional<MemberCourseLecture> memberCourseLecture = courseLectureService
        .getCourseLectureByMemberAndId(member, course.getCourseId());
    totalTime = 0;

    memberCourseLecture.ifPresentOrElse(
        info -> {
          HashMap<Long, MemberLecture> memberLectures;
          AddMemberLecture addMemberLecture = updateMemberCourseLecture(lectures,
              info.getMemberLectureMap());

          totalTime = addMemberLecture.getTime();
          memberLectures = addMemberLecture.getMemberLectures();

          info.updateMemberLecture(memberLectures);
        }, () -> {
          HashMap<Long, MemberLecture> memberLectures = new HashMap<>();

          for (Lecture lecture : lectures) {
            memberLectures.put(lecture.getLectureId(),
                new MemberLecture(lecture.getLectureTime(), LocalDateTime.now()));
            totalTime += lecture.getLectureTime();
          }

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
}
