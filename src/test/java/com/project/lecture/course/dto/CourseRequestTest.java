package com.project.lecture.course.dto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.course.dto.CourseRequest.Change;
import com.project.lecture.course.dto.CourseRequest.Create;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CourseRequestTest {

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

  //todo 매번 빈 값은 들어올 수 없다는 멘트를 날리는데 이는 어떤게 틀렸는지 알 수 없기에 나중에 커스텀 valid를 생각 해보자!
  @DisplayName("Create 요청 Valid[content]")
  @Test
  void createDtoCourse_valid_content() {
    // given
    Create req = CommonHelper.createCourseRequest();
    req.setCourseContent("");

    // when
    Set<ConstraintViolation<Create>> violations = validator.validate(req);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "빈 값은 들어올 수 없습니다.");
  }

  @DisplayName("Create 요청 Valid[name]")
  @Test
  void createDtoCourse_valid_name() {
    // given
    Create req = CommonHelper.createCourseRequest();
    req.setCourseName("");

    // when
    Set<ConstraintViolation<Create>> violations = validator.validate(req);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "빈 값은 들어올 수 없습니다.");
  }

  @DisplayName("Change 요청 Valid[content]")
  @Test
  void changeDtoCourse_valid_content() {
    // given
    Change req = CommonHelper.changeCourseRequest();
    req.setCourseId(1L);
    req.setCourseContent("");

    // when
    Set<ConstraintViolation<Change>> violations = validator.validate(req);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "빈 값은 들어올 수 없습니다.");
  }

  @DisplayName("Change 요청 Valid[name]")
  @Test
  void changeDtoCourse_valid_name() {
    // given
    Change req = CommonHelper.changeCourseRequest();
    req.setCourseId(1L);
    req.setCourseName("");

    // when
    Set<ConstraintViolation<Change>> violations = validator.validate(req);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "빈 값은 들어올 수 없습니다.");
  }

  @DisplayName("Change 요청 Valid[id]")
  @Test
  void changeDtoCourse_valid_id() {
    // given
    Change req = CommonHelper.changeCourseRequest();

    // when
    Set<ConstraintViolation<Change>> violations = validator.validate(req);
    ConstraintViolation[] result = violations.toArray(new ConstraintViolation[0]);

    // then
    assertThat(violations).isNotEmpty();
    assertEquals(result[0].getMessage(), "ID가 빈 값은 들어올 수 없습니다.");
  }
}