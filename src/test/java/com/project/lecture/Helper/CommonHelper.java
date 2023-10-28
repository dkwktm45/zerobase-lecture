package com.project.lecture.Helper;

import com.project.lecture.course.dto.CourseRequest;
import com.project.lecture.course.dto.CourseRequest.Create;
import com.project.lecture.course.dto.CreateLecture;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.type.AuthType;
import com.project.lecture.type.SocialType;
import com.project.lecture.user.dto.UserRequest;
import java.util.ArrayList;
import java.util.List;

public class CommonHelper {
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

  public static Course createCourseForm() {
    List<Lecture> list = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      list.add(Lecture.builder()
          .lectureTime(i)
          .lectureName(i + "번째 강의").build());
    }

    return Course.builder()
        .lectures(list)
        .courseName("제로베이스")
        .courseContent("제로베이스 백앤드").build();
  }
  public static Create createCourseCreateForm() {
    List<CreateLecture> list = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      list.add(new CreateLecture(i + "name", i));
    }

    return CourseRequest.Create.builder()
        .lectures(list)
        .courseName("제로베이스")
        .courseContent("제로베이스 백앤드").build();
  }
}
