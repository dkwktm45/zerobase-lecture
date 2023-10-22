package com.project.lecture.user.dto;

import com.project.lecture.entity.Member;
import com.project.lecture.type.AuthType;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SignUp{

    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Max(15)
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String password;

    @Max(10)
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String nickName;

    @Max(4)
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private AuthType authType;
    public Member toEntity(){
      return Member.builder()
          .email(this.email)
          .nickName(this.nickName)
          .password(this.password)
          .authType(this.authType.getDescription())
          .build();
    }
  }
}
