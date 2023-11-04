package com.project.lecture.api.study.controller;

import com.project.lecture.api.study.application.StudyApplication;
import com.project.lecture.api.study.dto.StudyDto;
import com.project.lecture.api.study.dto.StudyRequest;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.type.ResponseType;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/study")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class StudyController {

  private final StudyApplication studyApplication;
  private final StudyService studyService;

  @PostMapping("")
  public ResponseEntity<String> createStudyRequest(
      @RequestBody
      @Valid
      StudyRequest.Create request,
      Principal principal
  ) {
    studyApplication.createStudyByRequest(request, principal.getName());
    return ResponseEntity.ok(ResponseType.INSERT_SUCCESS.getDescription());
  }

  @GetMapping("")
  public ResponseEntity<String> completeStudyRequest(
      @RequestParam("studyId") Long id,
      Principal principal
  ) {
    studyService.completeStudyById(id, principal.getName());
    return ResponseEntity.ok(ResponseType.COMPLETE_SUCCESS.getDescription());
  }

  @PutMapping("")
  public ResponseEntity<String> updateStudyRequest(
      @RequestBody
      @Valid
      StudyRequest.Change request
  ) {
    studyApplication.changeStudyByRequest(request);
    return ResponseEntity.ok(ResponseType.CHANGE_SUCCESS.getDescription());
  }

  @DeleteMapping("{studyId}")
  public ResponseEntity<String> deleteStudyRequest(
      @PathVariable("studyId") Long studyId,
      Principal principal
  ) {
    studyApplication.deleteStudyById(studyId, principal.getName());
    return ResponseEntity.ok(ResponseType.DELETE_SUCCESS.getDescription());
  }

  @GetMapping("/list")
  public ResponseEntity<Page<StudyDto>> getListRequest(
      Principal principal,
      @PageableDefault(page = 1) Pageable pageable
  ) {
    return ResponseEntity.ok(
        studyApplication.getStudiesByEmail(principal.getName(),pageable)
    );
  }

  @GetMapping("{studyId}")
  public ResponseEntity<StudyDto> getStudyRequest(
      @PathVariable("studyId") Long id,
      Principal principal
  ) {
    return ResponseEntity.ok(
        studyApplication.getStudyByEmail(id, principal.getName())
    );
  }
}
