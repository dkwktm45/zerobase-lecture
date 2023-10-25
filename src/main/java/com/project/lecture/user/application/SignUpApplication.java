package com.project.lecture.user.application;

import com.project.lecture.exception.kind.ExceptionExistUser;
import com.project.lecture.type.ResponseType;
import com.project.lecture.user.dto.UserRequest;
import com.project.lecture.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
  private final PasswordEncoder passwordEncoder;
  private final MemberService memberService;
  public String saveUserByReq(UserRequest.SignUp sign) {
    if (memberService.hasEmail(sign.getEmail())) {
      throw new ExceptionExistUser(sign.getEmail());
    }
    sign.setPassword(passwordEncoder.encode(sign.getPassword()));

    memberService.createUser(
        sign.toEntity()
    );

    return ResponseType.SIGNUP_SUCCESS.getDescription();
  }
}
