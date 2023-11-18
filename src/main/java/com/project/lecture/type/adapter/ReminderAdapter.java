package com.project.lecture.type.adapter;

import com.project.lecture.api.planner.dto.StudyTypeDto;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Planner;
import com.project.lecture.entity.Reminder;
import com.project.lecture.exception.kind.ExceptionCompleteReminder;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.TypeContent;
import com.project.lecture.type.TypeRequest.Create;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Component
public class ReminderAdapter implements TypeAdapter {

  private final ReminderService reminderService;
  private final PlannerService plannerService;

  @Override
  public boolean existCheck(Create create, String email, Long memberId) {
    return reminderService.existsByIdAndEmail(create.getTypeId(), email);
  }

  @Override
  public TypeContent getContent(Long id) {
    return null;
  }

  @Override
  public void exceptionThrow() {
    throw new ExceptionNotFoundReminder();
  }

  @Override
  public void complete(Long id, Member member) {
    log.info("complete 수행");
    Reminder reminder = reminderService.getReminderById(id);

    if (!reminder.getMember().getEmail().equals(member.getEmail())) {
      throw new ExceptionNotFoundReminder();
    }

    if (reminder.isReminderComplete()) {
      throw new ExceptionCompleteReminder();
    }

    reminder.changeCompleteIntoTrue();
    log.info("complete 마침");
  }

  @Override
  public StudyType getStudyType() {
    return StudyType.REMINDER;
  }

  @Override
  public List<StudyTypeDto> getTypeStudies(Member member,boolean completeFlag) {
    List<Reminder> reminders = reminderService
        .getListByNoComplete(member, completeFlag);
    List<Planner> planners = plannerService
        .getPlannersByNoComplete(member, getStudyType(), completeFlag);

    return reminders.stream()
        .filter(reminder ->
            planners.stream()
                .noneMatch(planner -> reminder.getReminderId().equals(planner.getPlannerTypeId()))
        ).map(StudyTypeDto::toReminderDto).collect(Collectors.toList());
  }
}
