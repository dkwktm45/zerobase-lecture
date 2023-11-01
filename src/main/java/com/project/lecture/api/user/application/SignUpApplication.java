package com.project.lecture.api.user.application;

import com.project.lecture.api.user.dto.UserRequest.SignUp;
import com.project.lecture.exception.kind.ExceptionExistUser;
import com.project.lecture.api.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
  private final PasswordEncoder passwordEncoder;
  private final MemberService memberService;
  public void saveUserByReq(SignUp sign) {
    if (memberService.hasEmail(sign.getEmail())) {
      throw new ExceptionExistUser(sign.getEmail());
    }
    sign.setPassword(passwordEncoder.encode(sign.getPassword()));

    memberService.createUser(
        sign.toEntity()
    );
  }
}
