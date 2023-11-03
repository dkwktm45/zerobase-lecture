package com.project.lecture.repository;


import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reflection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
  @Test
  @DisplayName("Id 와 이메일을 통한 존재여부 확인 - true")
  void existsByReflectionIdAndMember_Email_true() {
    //given
    Member member = CommonHelper.createMemberFormByNoId();
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
}