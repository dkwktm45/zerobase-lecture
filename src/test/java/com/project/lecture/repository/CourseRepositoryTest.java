package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@ActiveProfiles("test")
class CourseRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private CourseRepository courseRepository;
  @Test
  @DisplayName("이메일과 일치하는 reflections를 반환한다.")
  void findByMember_Email(){
    //given
    Member member = CommonHelper.createOriginMemberFormByNoId();
    List<Course> courses = CommonHelper.createCourseListForm();
    memberRepository.save(member);
    courseRepository.saveAllAndFlush(courses);
    Pageable pageable = PageRequest.of(0,2);
    String email = "planner@gmail.com";

    //when
    Page<Course> resultPage = courseRepository.findByMember_Email(email, pageable);

    //then
    List<Course> result = resultPage.getContent();
    assertEquals(result.size(),2);
    for (int i = 0; i < result.size(); i++) {
      assertEquals(courses.get(i).getCourseContent(), result.get(i).getCourseContent());
      assertEquals(courses.get(i).getCourseName(), result.get(i).getCourseName());
    }
  }
}