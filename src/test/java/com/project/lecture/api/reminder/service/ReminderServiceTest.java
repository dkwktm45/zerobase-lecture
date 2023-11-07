package com.project.lecture.api.reminder.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Reminder;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import com.project.lecture.repository.ReminderRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

  @Mock
  private ReminderRepository reminderRepository;
  @InjectMocks
  private ReminderService reminderService;

  @Test
  @DisplayName("리마인드 저장")
  void saveReminderByEntity() {
    //given
    Reminder reminder = CommonHelper.createReminder();
    when(reminderRepository.save(reminder))
        .thenReturn(reminder);

    //when
    reminderService.saveReminderByEntity(reminder);

    //then
    verify(reminderRepository, timeout(1)).save(reminder);
  }

  @Test
  @DisplayName("id와 이메일을 통해 값이 있는지 확인")
  void existsByIdAndEmail() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";
    when(reminderRepository.existsByReminderIdAndMember_Email(anyLong(), anyString()))
        .thenReturn(true);
    //when
    boolean result = reminderService.existsByIdAndEmail(id, email);

    //then
    assertTrue(result);
  }

  @Test
  @DisplayName("id를 통한 삭제")
  void deleteReminderById() {
    //given
    Long id = 1L;
    doNothing().when(reminderRepository).deleteById(anyLong());

    //when
    reminderService.deleteReminderById(id);

    //then
    verify(reminderRepository, timeout(1)).deleteById(anyLong());
  }

  @Test
  @DisplayName("id에 해당하는 값 가져오기 - 성공")
  void getReminderById_success() {
    //given
    Long id = 1L;
    Reminder reminder = CommonHelper.createReminder();
    when(reminderRepository.findById(id))
        .thenReturn(Optional.of(reminder));
    //when
    Reminder result = reminderService.getReminderById(id);

    //then
    assertEquals(result.getReminderId(),reminder.getReminderId());
    assertEquals(result.getReminderTypeId(),reminder.getReminderTypeId());
    assertEquals(result.getReminderType(),reminder.getReminderType());
    assertEquals(result.isReminderComplete(),reminder.isReminderComplete());
  }

  @Test
  @DisplayName("id에 해당하는 값 가져오기 - 실패[empty]")
  void getReminderById_fail() {
    //given
    Long id = 1L;
    when(reminderRepository.findById(id))
        .thenReturn(Optional.empty());
    //when
    SuperException result = assertThrows(
        ExceptionNotFoundReminder.class,
        () -> reminderService.getReminderById(id)
    );

    //then
    assertEquals(result.getMessage(),"존재하지 않는 리마인드입니다.");
  }

  @Test
  @DisplayName("email 에 해당하는 여러 값 가져오기")
  void getListByEmailAndPage() {
    //given
    Page<Reminder> returnPage = new PageImpl<>(CommonHelper.createRemindersByNoId());

    when(reminderRepository.findByMember_Email(anyString(),any()))
        .thenReturn(returnPage);

    //when
    Page<Reminder> reminderPage = reminderService.getListByEmailAndPage(anyString(), any());

    //then
    List<Reminder> result = reminderPage.getContent();
    List<Reminder> reminders = returnPage.getContent();
    for (int i = 0; i < reminderPage.getSize(); i++) {
      assertEquals(result.get(i).getReminderId(), reminders.get(i).getReminderId());
      assertEquals(result.get(i).getReminderTypeId(), reminders.get(i).getReminderTypeId());
      assertEquals(result.get(i).getReminderType(), reminders.get(i).getReminderType());
    }
  }
}