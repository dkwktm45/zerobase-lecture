package com.project.lecture.type.adapter;

import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import com.project.lecture.exception.kind.ExceptionCompleteReminder;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import com.project.lecture.type.TypeContent;
import com.project.lecture.type.TypeRequest.Create;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReminderAdapter implements TypeAdapter {

  private final ReminderService reminderService;

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
}
