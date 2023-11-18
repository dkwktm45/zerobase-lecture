package com.project.lecture.api.planner.controller;

import com.project.lecture.api.planner.application.PlannerApplication;
import com.project.lecture.api.planner.dto.StudyTypeDto;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/studies")
public class PlannerStudiesController {

  private final PlannerApplication plannerApplication;

  @GetMapping("/")
  public ResponseEntity<Page<StudyTypeDto>> getStudiesRequest(
      Principal principal,
      Pageable pageable
  ) {
    return ResponseEntity.ok(
        plannerApplication.getStudiesList(pageable, principal.getName())
    );
  }
}
