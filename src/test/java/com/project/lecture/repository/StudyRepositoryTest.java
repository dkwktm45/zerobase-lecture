package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Study;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
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
  @Test
  @DisplayName("이메일과 일치하는 studies를 반환한다.")
  void findByMember_Email(){
    //given
    List<Study> studies = CommonHelper.createStudyListByNoId();
    Member member = CommonHelper.createOriginMemberFormByNoId();
    memberRepository.saveAndFlush(member);
    studyRepository.saveAllAndFlush(studies);
    Pageable pageable = PageRequest.of(0,2);
    String email = "planner@gmail.com";

    //when
    Page<Study> resultPage = studyRepository.findByMember_Email(email, pageable);

    //then
    List<Study> result = resultPage.getContent();
    assertEquals(result.size(),2);
    for (int i = 0; i < result.size(); i++) {
      assertEquals(studies.get(i).isStudyComplete(), result.get(i).isStudyComplete());
      assertEquals(studies.get(i).getStudyContent(), result.get(i).getStudyContent());
      assertEquals(studies.get(i).getStudyTitle(), result.get(i).getStudyTitle());
    }
  }
  @Autowired
  private EntityManager em;
  @AfterEach
  void setUp() {
    em.createNativeQuery("ALTER TABLE member ALTER COLUMN `memberId` RESTART                                                                     WITH 1")
        .executeUpdate();
  }
}