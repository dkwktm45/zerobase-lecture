package com.project.lecture.api.reminder.application;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.reminder.dto.ReminderDto;
import com.project.lecture.api.reminder.dto.ReminderRequest.Create;
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
import com.project.lecture.type.adapter.CourseAdapter;
import com.project.lecture.type.adapter.LectureAdapter;
import com.project.lecture.type.adapter.StudyAdapter;
import com.project.lecture.type.adapter.TypeAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
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
  private final LectureService lectureService;
  private final CourseService courseService;
  private final ListenService listenService;
  private Map<StudyType, TypeAdapter> adapters;

  @PostConstruct
  public void setUp() {
    adapters = new HashMap<>();
    adapters.put(StudyType.STUDY, new StudyAdapter(studyService));
    adapters.put(StudyType.COURSE, new CourseAdapter(listenService));
    adapters.put(StudyType.LECTURE, new LectureAdapter(listenService, lectureService));
  }
  public void createReminderByRequestAndEmail(Create request, String email) {
    Member member = memberService.getMemberByEmail(email);

    TypeAdapter adapter = adapters.get(request.getReminderType());
    if (adapter != null && !adapter.existCheck(request, email, member.getMemberId())) {
      handleReminderNotFound(request.getReminderType());
    }

    reminderService.saveReminderByEntity(
        Reminder.builder()
            .member(member)
            .reminderType(request.getReminderType())
            .reminderTypeId(request.getReminderTypeId())
            .build()
    );
  }

  private void handleReminderNotFound(StudyType studyType) {
    switch (studyType) {
      case STUDY:
        throw new ExceptionNotFoundStudy();
      case COURSE:
        throw new ExceptionExistListening();
      case LECTURE:
        throw new ExceptionExistListening();
      default:
        throw new UnsupportedOperationException("타당하지 않는 타입입니다.");
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

  public ReminderDto getByIdAndEmail(Long id, String email) {
    if (!reminderService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReminder();
    }

    Reminder reminder = reminderService.getReminderById(id);
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

    return ReminderDto.toDto(reminder,title,content);
  }

}
