package com.project.lecture.type.adapter;

import com.project.lecture.api.reminder.dto.ReminderRequest.Create;

public interface TypeAdapter {
  boolean existCheck(Create create, String email, Long id);
}
