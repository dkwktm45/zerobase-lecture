package com.project.lecture.type.adapter;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.reminder.dto.ReminderRequest.Create;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseAdapter implements TypeAdapter {
  private final ListenService listenService;
  @Override
  public boolean existCheck(Create create, String email, Long memberId) {
    return listenService.existCheck(create.getReminderTypeId(), memberId);
  }
}
