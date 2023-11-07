package com.project.lecture.api.reminder.application;

import static com.project.lecture.type.StudyType.COURSE;
import static com.project.lecture.type.StudyType.LECTURE;
import static com.project.lecture.type.StudyType.STUDY;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.reminder.dto.ReminderDto;
import com.project.lecture.api.reminder.dto.ReminderRequest.Create;
import com.project.lecture.api.reminder.dto.TypeContent;
import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.kind.ExceptionCompleteReminder;
import com.project.lecture.exception.kind.ExceptionExistListening;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import com.project.lecture.type.StudyType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReminderApplication {

  private final ReminderService reminderService;
  private final MemberService memberService;

  private final PlannerService plannerService;
  private final StudyService studyService;
  private final CourseService courseService;
  private final LectureService lectureService;
  private final ListenService listenService;

  public void createReminderByRequestAndEmail(Create request, String email) {
    Member member = memberService.getMemberByEmail(email);

    existsTypeCheck(request, email, member.getMemberId());

    reminderService.saveReminderByEntity(
        Reminder.builder()
            .member(member)
            .reminderType(request.getReminderType())
            .reminderTypeId(request.getReminderTypeId())
            .build()
    );
  }

  private void existsTypeCheck(Create request, String email, Long id) {
    if (request.getReminderType().equals(STUDY)) {
      if (!studyService.existStudyByIdAndEmail(
          request.getReminderTypeId(),
          email
      )) {
        throw new ExceptionNotFoundStudy();
      }
    } else if (request.getReminderType().equals(COURSE)) {
      if (!listenService.existCheck(
          request.getReminderTypeId(),
          id
      )) {
        throw new ExceptionExistListening();
      }
    } else if (request.getReminderType().equals(LECTURE)) {
      Lecture lecture = lectureService.getLectureById(request.getReminderTypeId());

      if (!listenService.existCheck(
          lecture.getCourse().getCourseId(),
          id
      )) {
        throw new ExceptionExistListening();
      }
    }
  }

  public void deleteReminderByIdAndEmail(Long id, String email) {
    if (!reminderService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReminder();
    }

    reminderService.deleteReminderById(id);

    if (plannerService.existByStudyIdAndType(id, StudyType.REMINDER)) {
      plannerService.deletePlanner(id, StudyType.REMINDER);
    }
  }

  public void completeByIdAndEmail(Long id, String email) {
    if (!reminderService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReminder();
    }

    Reminder reminder = reminderService.getReminderById(id);

    if (reminder.isReminderComplete()) {
      throw new ExceptionCompleteReminder();
    }

    reminder.changeCompleteIntoTrue();
  }

  private TypeContent getTypeContent(Reminder reminder) {
    String title = "";
    String content = "";
    switch (reminder.getReminderType()) {
      case STUDY:
        Study study = studyService.getStudyById(reminder.getReminderTypeId());
        title = study.getStudyTitle();
        content = study.getStudyContent();
        break;
      case COURSE:
        Course course = courseService.getCourseById(reminder.getReminderTypeId());
        title = course.getCourseName();
        content = course.getCourseContent();
        break;
      case LECTURE:
        Lecture lecture = lectureService.getLectureById(reminder.getReminderTypeId());
        title = lecture.getLectureName();
        break;
    }

    return new TypeContent(title, content);
  }

  public ReminderDto getByIdAndEmail(Long id, String email) {
    if (!reminderService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReminder();
    }

    Reminder reminder = reminderService.getReminderById(id);
    TypeContent typeContent = getTypeContent(reminder);

    return ReminderDto.toDto(reminder, typeContent.getTitle(), typeContent.getContent());
  }

  public Page<ReminderDto> getListByEmail(String email, Pageable pageable) {
    Page<Reminder> reminderPage = reminderService.getListByEmailAndPage(email, pageable);

    List<Reminder> reminders = reminderPage.getContent();
    List<ReminderDto> reminderDtos = new ArrayList<>();

    for (Reminder reminder : reminders) {

      TypeContent typeContent = getTypeContent(reminder);

      reminderDtos.add(
          ReminderDto.toDto(reminder, typeContent.getTitle(), typeContent.getContent())
          );
    }
    return new PageImpl<>(reminderDtos);
  }
}
