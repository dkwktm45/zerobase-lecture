package com.project.lecture.Helper;

import com.project.lecture.entity.Member;
import com.project.lecture.type.AuthType;
import com.project.lecture.type.SocialType;
import com.project.lecture.user.dto.UserRequest;

public class UserHelper {
  public static UserRequest.SignUp createSignUpForm(){
    return UserRequest.SignUp.builder()
        .email("wpekdl153@gmail.com")
        .password("1234")
        .nickName("게드릉")
        .authType(AuthType.USER).build();
  }



  public static Member createMemberForm() {
    return Member.builder()
        .email("planner@gmail.com")
        .password("1234")
        .nickName("게드릉")
        .socialType(SocialType.PLANNER)
        .socialId("1234")
        .authType(AuthType.USER.getDescription()).build();
  }
}
