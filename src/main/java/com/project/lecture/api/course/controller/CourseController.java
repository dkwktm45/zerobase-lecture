package com.project.lecture.api.course.controller;

import com.project.lecture.api.course.application.CourseApplication;
import com.project.lecture.api.course.dto.CourseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/course/inquiry")
public class CourseController {

  private final CourseApplication courseApplication;
  @GetMapping("/list")
  public ResponseEntity<Page<CourseDto>> getCoursesRequest(
      Pageable pageable
  ) {
    log.info("getCoursesRequest() 수행");
    return ResponseEntity.ok(
        courseApplication.getCoursesList(pageable)
    );
  }

  @GetMapping("")
  public ResponseEntity<CourseDto> getCourseRequest(
      @RequestParam("courseId") Long id
  ) {
    log.info("getCourseRequest() 수행");
    return ResponseEntity.ok(
        courseApplication.getCourse(id)
    );
  }
}
