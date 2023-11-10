package com.project.lecture.repository;

import static com.project.lecture.Helper.CommonHelper.createListingByMemberAndCourse;
import static com.project.lecture.Helper.CommonHelper.createListingForm;
import static com.project.lecture.Helper.CommonHelper.createOnlyCourseForm;
import static com.project.lecture.Helper.CommonHelper.createOriginMemberFormByNoId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project.lecture.entity.Course;
import com.project.lecture.entity.Listening;
import com.project.lecture.entity.Member;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class ListeningRepositoryTest {

  @Autowired
  private ListeningRepository listeningRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private CourseRepository courseRepository;


  @Test
  @DisplayName("member_id 및 course_id를 통한 값이 존재하는지 여부 - true")
  void existsByMemberIdAndCourseId_true() {
    // given
    Listening listening = createListingForm();
    Member member = createOriginMemberFormByNoId();
    Course course = createOnlyCourseForm();
    memberRepository.save(member);
    courseRepository.save(course);
    listeningRepository.save(listening);

    // when
    boolean result = listeningRepository.existsByMember_MemberIdAndCourse_CourseId(
        listening.getMember().getMemberId()
        , listening.getCourse().getCourseId());

    // then
    assertTrue(result);
  }
  @Test
  @DisplayName("member_id 및 course_id를 통한 값이 존재하는지 여부 - false")
  void existsByMemberIdAndCourseId_false() {
    // given
    // when
    boolean result = listeningRepository.existsByMember_MemberIdAndCourse_CourseId(
        1L
        , 1L);

    // then
    assertFalse(result);
  }
  @Test
  @DisplayName("member_id와 course_id에 맞는 컬럼 삭제")
  void deleteByMemberIdAndCourseId() {
    // given
    Listening listening = createListingByMemberAndCourse();
    Member member = createOriginMemberFormByNoId();
    Course course = createOnlyCourseForm();
    memberRepository.save(member);
    courseRepository.save(course);
    listeningRepository.save(listening);
    // when
    listeningRepository.deleteByMemberIdAndCourseId(
       1L
        , 1L);
    // then
    Long count = listeningRepository.count();
    assertEquals(count,0L);
  }
  @Autowired
  private EntityManager em;

  @AfterEach
  void setUp() {
    em.createNativeQuery(
            "ALTER TABLE member ALTER COLUMN `memberId` RESTART                                                                     WITH 1")
        .executeUpdate();
    em.createNativeQuery(
            "ALTER TABLE course ALTER COLUMN `courseId` RESTART                                                                     WITH 1")
        .executeUpdate();

  }
}