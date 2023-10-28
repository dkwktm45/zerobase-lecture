package com.project.lecture.course.dto;

import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateLecture {

  private String lectureName;

  @NotBlank(message = "빈 값은 들어올 수 없습니다.")
  @Min(value = 1, message = "강의 최소 시간을 준수 해야합니다.")
  @Max(value = 1000, message = "강의 최대 시간을 준수 해야합니다.")
  private int lectureTime;

  public static Lecture toEntity(CreateLecture lectureDto, Course course){
    return Lecture.builder()
        .lectureName(lectureDto.lectureName)
        .course(course)
        .lectureTime(lectureDto.lectureTime).build();
  }
}
