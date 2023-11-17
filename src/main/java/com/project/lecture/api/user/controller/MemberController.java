package com.project.lecture.api.user.controller;

import com.project.lecture.api.user.application.MemberApplication;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class MemberController {

  private final MemberApplication memberApplication;

  @GetMapping("/user/tier")
  public ResponseEntity getUserTierRequest(Principal principal){
    return ResponseEntity.ok(memberApplication.getUserTier(principal.getName()));
  }

}
