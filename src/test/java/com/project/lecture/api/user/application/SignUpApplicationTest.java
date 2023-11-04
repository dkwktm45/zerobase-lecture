package com.project.lecture.api.user.application;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.user.dto.UserRequest;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.exception.SuperException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
@ExtendWith(MockitoExtension.class)
class SignUpApplicationTest {
  @Mock
  private MemberService memberService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @InjectMocks
  private SignUpApplication signUpApplication;
  @Test
  @DisplayName("회원 가입 - 성공")
  void saveUserByReq_success() {
    UserRequest.SignUp sign = CommonHelper.createSignUpForm();
    // given
    when(memberService.hasEmail(anyString()))
        .thenReturn(false);
    when(passwordEncoder.encode(anyString()))
        .thenReturn("1353151");
    doNothing().when(memberService).createUser(any());

    // when
    signUpApplication.saveUserByReq(sign);


    // then
    verify(memberService, timeout(1)).hasEmail(anyString());
    verify(passwordEncoder, timeout(1)).encode(anyString());
    verify(memberService, timeout(1)).createUser(any());
  }

  @Test
  @DisplayName("회원 가입 - 실패[이메일]")
  void saveUserByReq_fail_email() {
    UserRequest.SignUp sign = CommonHelper.createSignUpForm();
    // given
    when(memberService.hasEmail(anyString()))
        .thenReturn(true);

    // when
    SuperException exception = assertThrows(SuperException.class,
        () -> signUpApplication.saveUserByReq(sign));

    // then
    assertEquals(exception.getMessage(),"이미 존재하는 이메일입니다.");
    verify(memberService, timeout(1)).hasEmail(anyString());
  }
}