package com.project.lecture.api.course.controller;


import static com.project.lecture.type.ResponseType.CHANGE_SUCCESS;
import static com.project.lecture.type.ResponseType.INSERT_SUCCESS;
import static com.project.lecture.type.ResponseType.DELETE_SUCCESS;

import com.project.lecture.api.course.application.AdminCourseApplication;
import com.project.lecture.api.course.dto.CourseDto;
import com.project.lecture.api.course.dto.CourseRequest;
import com.project.lecture.api.course.service.CourseService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/course")
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminCourseController {

  private final AdminCourseApplication adminCourseApplication;
  private final CourseService courseService;

  @PostMapping("/create")
  public ResponseEntity<String> createCourseRequest(
      @RequestBody @Valid CourseRequest.Create request,
      Principal principal
  ) {
    log.info("createCourseRequest() 수행");
    adminCourseApplication.createCourseAndLecture(request, principal.getName());
    return ResponseEntity.ok(INSERT_SUCCESS.getDescription());
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteCourseRequest(
      @RequestParam Long courseId
  ) {
    log.info("deleteCourseRequest() 수행");
    courseService.deleteCourseAndLectureById(courseId);
    return ResponseEntity.ok(DELETE_SUCCESS.getDescription());
  }

  @PutMapping("/change")
  public ResponseEntity<String> changeCourseRequest(
      @RequestBody @Valid
      CourseRequest.Change request
  ) {
    log.info("changeCourseRequest() 수행");
    courseService.changeCourseByForm(request);
    return ResponseEntity.ok(CHANGE_SUCCESS.getDescription());
  }

  @PostMapping("")
  public ResponseEntity<Page<CourseDto>> getCourseRequest(
      Principal principal,
      @PageableDefault(page = 1) Pageable pageable
  ) {
    log.info("getCourseRequest() 수행");
    return ResponseEntity.ok(
        adminCourseApplication.getCourseList(principal.getName(),pageable)
    );
  }
}
