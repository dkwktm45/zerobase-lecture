package com.project.lecture.Listen.controller;

import static com.project.lecture.type.ResponseType.COURSE_SUCCESS;

import com.project.lecture.Listen.application.ListenApplication;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/course/")
public class ListenController {

  private final ListenApplication listenApplication;

  @PutMapping("/listen/{courseId}")
  public ResponseEntity<?> userListenRequest(
      @PathVariable("courseId") Long courseId,
      Principal principal
  ) {
    listenApplication.listenCourse(courseId, principal.getName());

    return ResponseEntity.ok(
        COURSE_SUCCESS
    );
  }

}
