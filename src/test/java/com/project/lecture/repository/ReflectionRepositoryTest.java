package com.project.lecture.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reflection;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension.class)
class ReflectionRepositoryTest {

  @Autowired
  private ReflectionRepository reflectionRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Test
  @DisplayName("Id 와 이메일을 통한 존재여부 확인 - true")
  void existsByReflectionIdAndMember_Email_true() {
    //given
    Member member = CommonHelper.createMemberFormByNoId();
    memberRepository.save(member);
    Reflection reflection = CommonHelper.createReflectionByNoId();
    reflectionRepository.saveAndFlush(reflection);

    //when
    boolean result = reflectionRepository.existsByReflectionIdAndMember_Email(
        1L,member.getEmail()
    );

    //then
    Assertions.assertTrue(result);
  }

  @Test
  @DisplayName("Id 와 이메일을 통한 존재여부 확인 - false")
  void existsByReflectionIdAndMember_Email_false() {
    //given
    Member member = CommonHelper.createMemberFormByNoId();

    //when
    boolean result = reflectionRepository.existsByReflectionIdAndMember_Email(
        1L,member.getEmail()
    );

    //then
    Assertions.assertFalse(result);
  }
  @Test
  @DisplayName("이메일과 일치하는 reflections를 반환한다.")
  void findByMember_Email(){
    //given
    Member member = CommonHelper.createOriginMemberFormByNoId();
    List<Reflection> reflections = CommonHelper.createReflections();
    memberRepository.save(member);
    reflectionRepository.saveAllAndFlush(reflections);
    Pageable pageable = PageRequest.of(0,2);
    String email = "planner@gmail.com";

    //when
    Page<Reflection> resultPage = reflectionRepository.findByMember_Email(email, pageable);

    //then
    List<Reflection> result = resultPage.getContent();
    assertEquals(result.size(),2);
    for (int i = 0; i < result.size(); i++) {
      assertEquals(reflections.get(i).getReflectionTitle(), result.get(i).getReflectionTitle());
      assertEquals(reflections.get(i).getReflectionContent(), result.get(i).getReflectionContent());
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