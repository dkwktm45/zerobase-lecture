package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Listening;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class ListeningRepositoryTest {

  @Autowired
  private ListeningRepository listeningRepository;

  @Test
  @DisplayName("member_id 및 course_id를 통한 값이 존재하는지 여부 - true")
  void existsByMemberIdAndCourseId_true() {
    // given
    Listening listening = CommonHelper.createListingForm();
    listeningRepository.save(listening);

    // when
    boolean result = listeningRepository.existsByMemberIdAndCourseId(
        listening.getMember().getMemberId()
        , listening.getCourse().getCourseId());

    // then
    assertTrue(result);
  }
  @Test
  @DisplayName("member_id 및 course_id를 통한 값이 존재하는지 여부 - false")
  void existsByMemberIdAndCourseId_false() {
    // given
    Listening listening = CommonHelper.createListingForm();

    // when
    boolean result = listeningRepository.existsByMemberIdAndCourseId(
        listening.getMember().getMemberId()
        , listening.getCourse().getCourseId());

    // then
    assertFalse(result);
  }
  @Test
  @DisplayName("member_id와 course_id에 맞는 컬럼 삭제")
  void deleteByMemberIdAndCourseId() {
    // given
    Listening listening = CommonHelper.createListingForm();
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