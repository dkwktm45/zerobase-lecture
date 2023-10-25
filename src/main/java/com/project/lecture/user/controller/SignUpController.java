package com.project.lecture.user.controller;

import com.project.lecture.user.application.SignUpApplication;
import com.project.lecture.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/join")
public class SignUpController {

  private final SignUpApplication signUpApplication;

  @PostMapping("")
  public ResponseEntity<String> joinRequest(
      @RequestBody
      UserRequest.SignUp request
  ) {
    return ResponseEntity.ok(signUpApplication.saveUserByReq(request));
  }
}
