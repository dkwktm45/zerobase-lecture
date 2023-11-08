package com.project.lecture.api.complete.controller;

import com.project.lecture.api.complete.application.CompleteApplication;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/complete")
@PreAuthorize("hasAuthority('USER')")
public class CompleteController {

  private final CompleteApplication completeApplication;

  @PostMapping("/course")
  public ResponseEntity<Void> completeCourseRequest(
      @RequestParam("courseId") Long courseId,
      Principal principal
  ) {
    completeApplication.completeCourseByIdAndEmail(courseId, principal.getName());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/lecture")
  public ResponseEntity<Void> completeLectureRequest(
      @RequestParam("lectureId") Long lectureId,
      Principal principal
  ) {
    completeApplication.completeLectureByIdAndEmail(lectureId, principal.getName());
    return ResponseEntity.ok().build();
  }
}
