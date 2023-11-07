package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  @Test
  @DisplayName("email 에 해당하는 여러 값이 있는지 확인 ")
  void findByMember_Email() {
    //given
    Member member = CommonHelper.createMemberFormByNoId();
    memberRepository.save(member);
    List<Reminder> reminders = CommonHelper.createRemindersByNoId();
    reminderRepository.saveAll(reminders);
    String email = "planner@gmai.com";
    Pageable pageable = PageRequest.of(0, 2);
    //when
    Page<Reminder> result = reminderRepository.findByMember_Email(email, pageable);

    //then
    assertEquals(result.getSize(),2);
    List<Reminder> returnReminders = result.getContent();
    for (int i = 0; i < returnReminders.size(); i++) {
      assertEquals(reminders.get(i).getReminderType(),
          returnReminders.get(i).getReminderType());
      assertEquals(reminders.get(i).getReminderTypeId(),
          returnReminders.get(i).getReminderTypeId());
      assertEquals(reminders.get(i).isReminderComplete(),
          returnReminders.get(i).isReminderComplete());
    }
  }
}