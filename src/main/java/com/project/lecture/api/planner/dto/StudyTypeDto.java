package com.project.lecture.api.planner.dto;

import com.project.lecture.entity.Course;
import com.project.lecture.entity.Reminder;
import com.project.lecture.entity.Study;
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
public class StudyTypeDto {
  private Long typeId;
  private String title;
  private StudyType type;

  public static StudyTypeDto toCourseDto(Course course) {
    return StudyTypeDto.builder()
        .title(course.getCourseName())
        .typeId(course.getCourseId())
        .type(StudyType.COURSE)
        .build();
  }

  public static StudyTypeDto toReminderDto(Reminder reminder) {
    return StudyTypeDto.builder()
        .typeId(reminder.getReminderId())
        .type(StudyType.REMINDER)
        .build();
  }

  public static StudyTypeDto toStudyDto(Study study) {
    return StudyTypeDto.builder()
        .typeId(study.getStudyId())
        .title(study.getStudyTitle())
        .type(StudyType.STUDY)
        .build();
  }
}
