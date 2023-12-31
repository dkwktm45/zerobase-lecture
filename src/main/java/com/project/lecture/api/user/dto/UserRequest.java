package com.project.lecture.api.user.dto;

import com.project.lecture.entity.Member;
import com.project.lecture.type.AuthType;
import com.project.lecture.type.SocialType;
import com.project.lecture.type.valid.ValidEnum;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

public class UserRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignUp{

    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Length(max = 15, message = "비밀번호 길이를 초과했습니다.")
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String password;

    @Length(max = 10, message = "닉네임 길이를 초과하셨습니다.")
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String nickName;

    @ValidEnum(enumClass = AuthType.class)
    private AuthType authType;
    public Member toEntity(){
      return Member.builder()
          .email(this.email)
          .nickName(this.nickName)
          .password(this.password)
          .socialType(SocialType.PLANNER)
          .authType(this.authType.getDescription())
          .build();
    }
  }
}
