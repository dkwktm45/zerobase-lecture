package com.project.lecture.api.planner.controller;

import com.project.lecture.api.planner.application.PlannerApplication;
import com.project.lecture.api.planner.dto.PlannerRequest;
import com.project.lecture.type.TypeRequest;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/planner")
public class PlannerController {

  private final PlannerApplication plannerApplication;

  @PostMapping("")
  public ResponseEntity<Void> createPlannerRequest(
      @RequestBody @Valid TypeRequest.Create request,
      Principal principal
  ) {
    plannerApplication.createPlannerByRequestAndEmail(request, principal.getName());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/update")
  public ResponseEntity<String> updatePlannerRequest(
      @RequestBody @Valid PlannerRequest.Update request,
      Principal principal
  ) {
    plannerApplication.updatePlannerByRequestAndEmail(request, principal.getName());
    return ResponseEntity.ok().build();
  }

  @GetMapping("")
  public ResponseEntity<Void> completePlannerRequest(
      @RequestParam("plannerId") Long id,
      Principal principal
  ) {
    plannerApplication.completePlannerByIdAndEmail(id, principal.getName());
    return ResponseEntity.ok().build();
  }
}
