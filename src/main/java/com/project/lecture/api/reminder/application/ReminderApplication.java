package com.project.lecture.api.reminder.application;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.reminder.dto.ReminderDto;
import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import com.project.lecture.exception.kind.ExceptionCompleteReminder;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.TypeContent;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.adapter.TypeAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
  private final LectureService lectureService;
  private final CourseService courseService;
  private final ListenService listenService;
  private final Map<StudyType, TypeAdapter> adapterMap;


  public void createReminderByRequestAndEmail(Create request, String email) {
    Member member = memberService.getMemberByEmail(email);

    TypeAdapter adapter = adapterMap.get(request.getType());

    if (adapter == null){
      throw new UnsupportedOperationException("타당하지 않는 타입입니다.");
    }
    if (!adapter.existCheck(request, email, member.getMemberId())) {
      adapter.exceptionThrow();
    }

    reminderService.saveReminderByEntity(
        Reminder.builder()
            .member(member)
            .reminderType(request.getType())
            .reminderTypeId(request.getTypeId())
            .build()
    );
  }

  public void deleteReminderByIdAndEmail(Long id, String email) {
    if (!reminderService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReminder();
    }

    reminderService.deleteReminderById(id);

    plannerService.getPlannerByStudyIdAndType(id, StudyType.REMINDER)
        .ifPresent(plannerService::deletePlanner);
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
    TypeContent typeContent = adapterMap.get(reminder.getReminderType())
        .getContent(reminder.getReminderTypeId());

    return ReminderDto.toDto(reminder, typeContent.getTitle(), typeContent.getContent());
  }

  public Page<ReminderDto> getListByEmail(String email, Pageable pageable) {
    Page<Reminder> reminderPage = reminderService.getListByEmailAndPage(email, pageable);

    List<Reminder> reminders = reminderPage.getContent();
    List<ReminderDto> reminderDtos = new ArrayList<>();

    for (Reminder reminder : reminders) {

      TypeContent typeContent = adapterMap.get(reminder.getReminderType())
          .getContent(reminder.getReminderTypeId());

      reminderDtos.add(
          ReminderDto.toDto(reminder, typeContent.getTitle(), typeContent.getContent())
      );
    }
    return new PageImpl<>(reminderDtos);
  }
}
