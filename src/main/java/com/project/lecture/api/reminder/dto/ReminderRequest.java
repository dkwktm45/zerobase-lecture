package com.project.lecture.api.reminder.dto;


import com.project.lecture.type.StudyType;
import com.project.lecture.type.valid.ValidEnum;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReminderRequest {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Create {
    @NotNull(message = "빈 값은 들어올 수 없습니다.")
    private Long reminderTypeId;
    @ValidEnum(enumClass = StudyType.class)
    @NotNull(message = "빈 값은 들어올 수 없습니다.")
    private StudyType reminderType;
  }
}
