package com.project.lecture.api.reminder.application;

import static com.project.lecture.type.StudyType.COURSE;
import static com.project.lecture.type.StudyType.LECTURE;
import static com.project.lecture.type.StudyType.STUDY;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.reminder.dto.ReminderRequest.Create;
import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import com.project.lecture.exception.kind.ExceptionCompleteReminder;
import com.project.lecture.exception.kind.ExceptionExistListening;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import com.project.lecture.exception.kind.ExceptionNotFoundStudy;
import com.project.lecture.type.StudyType;
import lombok.RequiredArgsConstructor;
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


}
