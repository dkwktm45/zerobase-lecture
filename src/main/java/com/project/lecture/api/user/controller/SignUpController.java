package com.project.lecture.api.user.controller;

import com.project.lecture.api.user.application.SignUpApplication;
import com.project.lecture.api.user.dto.UserRequest.SignUp;
import com.project.lecture.type.ResponseType;
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
      SignUp request
  ) {
    signUpApplication.saveUserByReq(request);
    return ResponseEntity.ok(
        ResponseType.SIGNUP_SUCCESS.getDescription()
    );
  }
}
