package com.project.lecture.api.course.application;

import com.project.lecture.api.course.dto.CourseDto;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseApplication {

  private final CourseService courseService;

  public Page<CourseDto> getCoursesList(Pageable pageable) {
    Page<Course> pageCourse = courseService.getListByPage(pageable);
    return CourseDto.toDotList(pageCourse);
  }

  public CourseDto getCourse(Long id) {
    return new CourseDto(courseService.getCourseById(id));
  }
}
