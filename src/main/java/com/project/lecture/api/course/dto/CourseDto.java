package com.project.lecture.api.course.dto;

import com.project.lecture.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

  private Long courseId;
  private String courseName;
  private String courseContent;
  private int listeningMembers;

  public CourseDto(Course course) {
    this.courseContent = course.getCourseContent();
    this.courseId = course.getCourseId();
    this.courseName = course.getCourseName();
    this.listeningMembers = course.getListenings().size();
  }

  public static Page<CourseDto> toDotList(Page<Course> courses){
    return courses.map(CourseDto::new);
  }
}
