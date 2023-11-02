package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Listening;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class ListeningRepositoryTest {

  @Autowired
  private ListeningRepository listeningRepository;
  @Autowired
  private TestEntityManager em;
  private Listening listening;

  @BeforeEach
  void setUp() {
    listening = CommonHelper.createListingForm();
    em.persistAndFlush(listening.getCourse());
    em.persistAndFlush(listening.getMember());
  }
  @Test
  @DisplayName("member_id 및 course_id를 통한 값이 존재하는지 여부 - true")
  void existsByMemberIdAndCourseId_true() {
    // given
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
        listening.getMember().getMemberId()
        , listening.getCourse().getCourseId());

    // then
    assertFalse(result);
  }
  @Test
  @DisplayName("member_id와 course_id에 맞는 컬럼 삭제")
  void deleteByMemberIdAndCourseId() {
    // given
    listeningRepository.save(listening);

    // when
    listeningRepository.deleteByMemberIdAndCourseId(
        listening.getMember().getMemberId()
        , listening.getCourse().getCourseId());
    // then
    Long count = listeningRepository.count();
    assertEquals(count,0L);
  }
}