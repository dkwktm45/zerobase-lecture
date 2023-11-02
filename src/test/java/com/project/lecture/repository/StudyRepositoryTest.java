package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@ActiveProfiles("test")
class StudyRepositoryTest {

  @Autowired
  private StudyRepository studyRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Test
  @DisplayName("email과 id를 통해서 값이 있는지 확인한다. - true")
  void existsByStudyIdAndMember_Email_true() {
    //given
    Study study = CommonHelper.createStudyByNoId();
    memberRepository.save(study.getMember());
    studyRepository.save(study);

    //when
    boolean result = studyRepository.existsByStudyIdAndMember_Email(
        1L,study.getMember().getEmail()
    );
    //then
    assertTrue(result);
  }
  @Test
  @DisplayName("email과 id를 통해서 값이 있는지 확인한다. - false")
  void existsByStudyIdAndMember_Email_false() {
    //given
    Study study = CommonHelper.createStudyByNoId();

    //when
    boolean result = studyRepository.existsByStudyIdAndMember_Email(
        1L,study.getMember().getEmail()
    );
    //then
    assertFalse(result);
  }
}