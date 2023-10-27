package com.project.lecture.course.controller;

import static com.project.lecture.type.ResponseType.COURSE_SUCCESS;

import com.project.lecture.course.application.CourseApplication;
import com.project.lecture.course.dto.CourseRequest;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/lecture")
@Slf4j
public class CourseController {

  private final CourseApplication courseApplication;

  @PostMapping("/create")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<String> createLectureRequest(
      @RequestBody @Valid CourseRequest.Create request,
      Principal principal
  ){
    log.info("createLectureRequest() 수행");
    courseApplication.createCourseAndLecture(request, principal.getName());
    return ResponseEntity.ok(COURSE_SUCCESS.getDescription());
  }
}
