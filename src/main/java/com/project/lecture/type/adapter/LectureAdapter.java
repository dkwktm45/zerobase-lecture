package com.project.lecture.type.adapter;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.reminder.dto.ReminderRequest.Create;
import com.project.lecture.entity.Lecture;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LectureAdapter implements TypeAdapter {
  private final ListenService listenService;
  private final LectureService lectureService;
  @Override
  public boolean existCheck(Create create, String email, Long memberId) {
    Lecture lecture = lectureService.getLectureById(create.getReminderTypeId());
    return listenService.existCheck(lecture.getCourse().getCourseId(), memberId);
  }
}
