package com.project.lecture.api.reminder.dto;

import com.project.lecture.entity.Reminder;
import com.project.lecture.type.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderDto {

  private Long reminderId;
  private String title;
  private String content;
  private Long reminderTypeId;
  private StudyType reminderType;

  public static ReminderDto toDto(Reminder reminder,String title, String content){
    return ReminderDto.builder()
        .content(content)
        .reminderId(reminder.getReminderId())
        .reminderTypeId(reminder.getReminderTypeId())
        .reminderType(reminder.getReminderType())
        .title(title)
        .build();
  }
}
