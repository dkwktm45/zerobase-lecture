package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ReminderRepositoryTest {

  @Autowired
  private ReminderRepository reminderRepository;
  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("id,email 에 해당하는 값이 있는지 확인 - true")
  void existsByReminderIdAndMember_Email_true() {
    //given
    String email = "planner@gmail.com";
    Long id = 1L;

    Member member = CommonHelper.createMemberFormByNoId();
    Reminder reminder = CommonHelper.createReminderByNoId();
    memberRepository.save(member);
    reminderRepository.save(reminder);

    //when
    boolean result = reminderRepository.existsByReminderIdAndMember_Email(id, email);

    //then
    assertTrue(result);
  }

  @Test
  @DisplayName("id,email 에 해당하는 값이 있는지 확인 - true")
  void existsByReminderIdAndMember_Email_false() {
    //given
    String email = "planner@gmail.com";
    Long id = 1L;

    //when
    boolean result = reminderRepository.existsByReminderIdAndMember_Email(id, email);

    //then
    assertFalse(result);
  }
}