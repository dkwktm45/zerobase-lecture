package com.project.lecture.type.adapter;

import com.project.lecture.api.reminder.dto.ReminderRequest.Create;
import com.project.lecture.api.study.service.StudyService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyAdapter implements TypeAdapter {

  private final StudyService studyService;

  @Override
  public boolean existCheck(Create create, String email, Long id) {
    return studyService.existStudyByIdAndEmail(create.getReminderTypeId(), email);
  }
}
