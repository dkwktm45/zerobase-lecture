package com.project.lecture.api.study.controller;

import com.project.lecture.api.study.application.StudyApplication;
import com.project.lecture.api.study.dto.StudyRequest;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.type.ResponseType;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/study")
@RequiredArgsConstructor
public class StudyController {

  private final StudyApplication studyApplication;
  private final StudyService studyService;
  @PutMapping("")
  public ResponseEntity<String> createStudyRequest(
      @RequestBody
      @Valid
      StudyRequest.Create request,
      Principal principal
  ) {
    studyApplication.createStudyByRequest(request,principal.getName());
    return ResponseEntity.ok(ResponseType.INSERT_SUCCESS.getDescription());
  }

  @GetMapping("")
  public ResponseEntity<String> updateStudyRequest(
      @RequestParam("studyId") Long id,
      Principal principal
  ) {
    studyService.completeStudyById(id,principal.getName());
    return ResponseEntity.ok(ResponseType.COMPLETE_SUCCESS.getDescription());
  }
}
