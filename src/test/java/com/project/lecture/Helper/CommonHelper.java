package com.project.lecture.Helper;

import com.project.lecture.api.course.dto.CourseRequest;
import com.project.lecture.api.course.dto.CourseRequest.Change;
import com.project.lecture.api.course.dto.CourseRequest.Create;
import com.project.lecture.api.course.dto.CreateLecture;
import com.project.lecture.api.reflection.dto.ReflectionRequest;
import com.project.lecture.type.TypeRequest;
import com.project.lecture.api.study.dto.StudyRequest;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Listening;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.entity.Planner;
import com.project.lecture.entity.Reflection;
import com.project.lecture.entity.Reminder;
import com.project.lecture.entity.Study;
import com.project.lecture.type.AuthType;
import com.project.lecture.type.SocialType;
import com.project.lecture.type.StudyType;
import com.project.lecture.api.user.dto.UserRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommonHelper {

  public static UserRequest.SignUp createSignUpForm() {
    return UserRequest.SignUp.builder()
        .email("wpekdl153@gmail.com")
        .password("1234")
        .nickName("게드릉")
        .authType(AuthType.USER).build();
  }

  public static Create createCourseRequest() {
    return Create.builder()
        .courseContent("제로베이스 백엔드과정")
        .courseName("제로베이스").build();
  }

  public static Change changeCourseRequest() {
    return Change.builder()
        .courseContent("제로베이스 백엔드과정")
        .courseName("제로베이스").build();
  }

  public static Member createMemberForm() {
    return Member.builder()
        .memberId(1L)
        .planners(new ArrayList<>())
        .email("planner@gmail.com")
        .password("1234")
        .nickName("게드릉")
        .socialType(SocialType.PLANNER)
        .socialId("1234")
        .studies(createStudyList())
        .courses(createCourseListForm())
        .authType(AuthType.USER.getDescription()).build();
  }


  public static Member createMemberFormByNoId() {
    return Member.builder()
        .planners(new ArrayList<>())
        .email("planner@gmail.com")
        .password("1234")
        .nickName("게드릉")
        .socialType(SocialType.PLANNER)
        .socialId("1234")
        .studies(createStudyList())
        .reflections(createReflections())
        .courses(createCourseListForm())
        .authType(AuthType.USER.getDescription()).build();
  }

  public static Member createOriginMemberFormByNoId() {
    return Member.builder()
        .email("planner@gmail.com")
        .password("1234")
        .nickName("게드릉")
        .socialType(SocialType.PLANNER)
        .socialId("1234")
        .authType(AuthType.USER.getDescription()).build();
  }
  public static Member createOriginMemberForm() {
    return Member.builder()
        .memberId(1L)
        .email("planner@gmail.com")
        .password("1234")
        .nickName("게드릉")
        .socialType(SocialType.PLANNER)
        .socialId("1234")
        .authType(AuthType.USER.getDescription()).build();
  }

  public static StudyRequest.Create studyRequestCreate() {
    return new StudyRequest.Create("title", "content");
  }

  public static Member createMemberAndPlannersForm() {
    return Member.builder()
        .memberId(1L)
        .planners(createPlannersAndCourseForm())
        .email("planner@gmail.com")
        .password("1234")
        .nickName("게드릉")
        .socialType(SocialType.PLANNER)
        .socialId("1234")
        .courses(createCourseListForm())
        .authType(AuthType.USER.getDescription()).build();
  }


  public static Course createCourseForm() {
    return Course.builder()
        .courseId(1L)
        .lectures(createLecturesNoIdForm())
        .courseName("제로베이스")
        .courseContent("제로베이스 백앤드").build();
  }


  public static List<Course> createCourseListForm() {
    List<Course> courses = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      courses.add(Course.builder()
          .courseName("제로베이스" + i)
          .member(createOriginMemberForm())
          .courseContent("제로베이스 백앤드" + i).build());
    }
    return courses;
  }

  public static List<Planner> createPlannersAndCourseForm() {
    List<Planner> planners = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      planners.add(
          Planner.builder()
              .plannerType(StudyType.COURSE)
              .plannerTypeId((long) i).build()
      );
    }
    return planners;
  }

  public static Planner createPlannerByCourseForm() {
    return Planner.builder()
        .plannerType(StudyType.COURSE)
        .plannerTypeId(1L).build();
  }

  public static Planner createPlannerByStudyForm() {
    return Planner.builder()
        .plannerType(StudyType.STUDY)
        .plannerTypeId(1L).build();
  }

  public static List<Planner> createPlannersForm() {
    List<Planner> planners = new ArrayList<>();
    LocalDate date = LocalDate.now();
    for (int i = 0; i < 3; i++) {
      planners.add(
          Planner.builder()
              .plannerTypeId((long) i)
              .member(createMemberForm())
              .plannerDt(date.plusDays(i))
              .plannerType(StudyType.LECTURE)
              .build()
      );
    }

    return planners;
  }

  public static Course createOnlyCourseForm() {
    return Course.builder()
        .courseName("제로베이스")
        .courseContent("제로베이스 백앤드").build();
  }

  public static List<Lecture> createLecturesForm() {
    List<Lecture> list = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      list.add(Lecture.builder()
          .lectureTime(i)
          .lectureId((long) i)
          .lectureName(i + "번째 강의").build());
    }
    return list;
  }

  public static List<Lecture> createLecturesNoIdForm() {
    List<Lecture> list = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      list.add(Lecture.builder()
          .lectureTime(i)
          .lectureName(i + "번째 강의").build());
    }
    return list;
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

  public static Change changeCourseForm() {
    return CourseRequest.Change.builder()
        .courseName("제로베이스")
        .courseContent("제로베이스 백앤드").build();
  }

  public static Listening createListingForm() {
    return Listening.builder()
        .member(createOriginMemberForm())
        .course(createCourseForm())
        .build();
  }
  public static Listening createListingByMemberAndCourse() {
    return Listening.builder()
        .member(createOriginMemberForm())
        .course(createCourseByNoLecture())
        .build();
  }


  public static Study createStudyByNoId() {
    return Study.builder()
        .studyTitle("title")
        .member(createMemberFormByNoId())
        .studyComplete(false)
        .studyContent("content").build();
  }

  public static Study createStudy() {
    return Study.builder()
        .studyId(1L)
        .studyTitle("title")
        .member(createMemberForm())
        .studyComplete(false)
        .studyContent("content").build();
  }

  public static Study createStudyCompleteTrue() {
    return Study.builder()
        .studyTitle("title")
        .member(createMemberForm())
        .studyComplete(true)
        .studyContent("content").build();
  }

  public static StudyRequest.Change changeStudyForm() {
    return new StudyRequest.Change(1L, "title", "content");
  }

  public static List<Study> createStudyList() {
    List<Study> studies = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      studies.add(
          Study.builder()
              .studyId((long) i)
              .studyTitle("title" + i)
              .studyComplete(false)
              .studyContent("content" + i)
              .build()
      );
    }
    return studies;
  }
  public static List<Study> createStudyListByNoId() {
    List<Study> studies = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      studies.add(
          Study.builder()
              .studyTitle("title" + i)
              .studyComplete(false)
              .member(createOriginMemberForm())
              .studyContent("content" + i)
              .build()
      );
    }
    return studies;
  }
  public static List<Reflection> createReflections() {
    List<Reflection> reflections = new ArrayList<>();

    for (int i = 1; i <= 3; i++) {
      reflections.add(
          Reflection.builder()
              .reflectionComplete(false)
              .reflectionTitle("title" + i)
              .member(createOriginMemberForm())
              .reflectionContent("content" + i).build()
      );
    }
    return reflections;
  }

  public static ReflectionRequest.Create reflectionRequestDto() {
    return new ReflectionRequest.Create("reTitle", "reContent", "2023-01-01/2023-01-08");
  }

  public static Reflection createReflectionByNoId() {
    return Reflection.builder()
        .reflectionTitle("reTitle")
        .member(createOriginMemberForm())
        .reflectionComplete(false)
        .reflectionContent("reContent").build();
  }

  public static Reflection createReflectionBy() {
    return Reflection.builder()
        .reflectionId(1L)
        .reflectionTitle("reTitle")
        .member(createMemberFormByNoId())
        .reflectionComplete(false)
        .reflectionContent("reContent").build();
  }

  public static TypeRequest.Create createReminderRequest() {
    return new TypeRequest.Create(1L,StudyType.REMINDER,null);
  }

  public static Reminder createReminder() {
    return Reminder.builder()
        .reminderId(1L)
        .reminderTypeId(1L)
        .reminderType(StudyType.STUDY)
        .reminderComplete(false)
        .build();
  }
  public static Reminder createReminderByNoId() {
    return Reminder.builder()
        .reminderTypeId(1L)
        .reminderType(StudyType.STUDY)
        .member(createOriginMemberForm())
        .reminderComplete(false)
        .build();
  }

  public static List<Reminder> createRemindersByNoId(){
    List<Reminder> reminders = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      reminders.add(
          Reminder.builder()
              .reminderTypeId((long) i)
              .reminderType(StudyType.STUDY)
              .member(createOriginMemberForm())
              .reminderComplete(false)
              .build()
      );
    }
    return reminders;
  }
  public static Reminder createReminderByCompleteTrue() {
    return Reminder.builder()
        .reminderId(1L)
        .reminderTypeId(1L)
        .reminderType(StudyType.STUDY)
        .reminderComplete(true)
        .build();
  }

  public static Lecture createLecture() {
    return Lecture.builder()
        .lectureId(1L)
        .course(createCourseByNoLecture())
        .lectureName("planner")
        .lectureTime(10).build();
  }

  private static Course createCourseByNoLecture() {
      return Course.builder()
          .courseId(1L)
          .courseName("제로베이스")
          .courseContent("제로베이스 백앤드").build();
  }

  public static MemberCourseLecture createMemberCourseLecture() {
    return MemberCourseLecture
        .builder()
        .memberLectures(new HashMap<>())
        .id(1L)
        .member(createOriginMemberForm())
        .build();
  }
}
