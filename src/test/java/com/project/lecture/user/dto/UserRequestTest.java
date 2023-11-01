package com.project.lecture.user.dto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.user.dto.UserRequest;
import com.project.lecture.api.user.dto.UserRequest.SignUp;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRequestTest {

  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @AfterAll
  public static void close() {
    factory.close();
  }

  @DisplayName("email에 빈문자열 전송 시 에러 발생")
  @Test
  void dtoEmail_valid() {
    // given
    UserRequest.SignUp signUpDto = CommonHelper.createSignUpForm();
    signUpDto.setEmail(" ");
    // when
    Set<ConstraintViolation<SignUp>> violations = validator.validate(signUpDto);

    boolean emailErrorFound = false;

    for (ConstraintViolation<SignUp> violation : violations) {
      String propertyPath = violation.getPropertyPath().toString();
      String errorMessage = violation.getMessage();

      if ("email".equals(propertyPath)) {
        assertTrue(
            errorMessage.equals("이메일 형식이 올바르지 않습니다.")
                || errorMessage.equals("빈 값은 들어올 수 없습니다."));
        emailErrorFound = true;
      } else {
        emailErrorFound = false;
      }
    }

    // then
    assertTrue(emailErrorFound);
  }

  @DisplayName("비밀번호 빈값이 들어올 때 발생 에러")
  @Test
  void dtoPassword_valid() {
    // given
    UserRequest.SignUp signUpDto = CommonHelper.createSignUpForm();
    signUpDto.setPassword("");
    // when
    Set<ConstraintViolation<SignUp>> violations = validator.validate(signUpDto);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "빈 값은 들어올 수 없습니다.");
  }

  @DisplayName("비밀번호 길이를 초과한 경우 발생 에러")
  @Test
  void dtoPassword_valid_length() {
    // given
    UserRequest.SignUp signUpDto = CommonHelper.createSignUpForm();
    signUpDto.setPassword("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ");
    // when
    Set<ConstraintViolation<SignUp>> violations = validator.validate(signUpDto);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "비밀번호 길이를 초과했습니다.");
  }

  @DisplayName("닉네임 빈값이 들어올 때 발생 에러")
  @Test
  void dtoNickName_valid() {
    // given
    UserRequest.SignUp signUpDto = CommonHelper.createSignUpForm();
    signUpDto.setNickName("");
    // when
    Set<ConstraintViolation<SignUp>> violations = validator.validate(signUpDto);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "빈 값은 들어올 수 없습니다.");
  }

  @DisplayName("닉네임 길이를 초과한 경우 발생 에러")
  @Test
  void dtoNickName_valid_length() {
    // given
    UserRequest.SignUp signUpDto = CommonHelper.createSignUpForm();
    signUpDto.setNickName("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ");
    // when
    Set<ConstraintViolation<SignUp>> violations = validator.validate(signUpDto);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "닉네임 길이를 초과하셨습니다.");
  }


}