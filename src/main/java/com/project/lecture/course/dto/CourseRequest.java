package com.project.lecture.course.dto;

import com.project.lecture.entity.Course;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CourseRequest {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {

    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String courseName;
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String courseContent;
    private List<LectureDto> lectures = new ArrayList<>();

    public Course toEntity(Create newCourse) {
      return Course.builder()
          .courseContent(newCourse.courseContent)
          .courseName(newCourse.courseName)
          .build();
    }
  }
}
