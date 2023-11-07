package com.project.lecture.api.reminder.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.reminder.dto.ReminderDto;
import com.project.lecture.api.reminder.dto.ReminderRequest;
import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionCompleteReminder;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class ReminderApplicationTest {

  @Mock
  private MemberService memberService;
  @Mock
  private ReminderService reminderService;
  @Mock
  private PlannerService plannerService;
  @Mock
  private StudyService studyService;

  @InjectMocks
  private ReminderApplication reminderApplication;

  @Test
  @DisplayName("request 와 email을 가지고 리마인더를 만든다.")
  void createReminderByRequestAndEmail() {
    //given
    Member member = CommonHelper.createOriginMemberForm();
    ReminderRequest.Create request = CommonHelper.createReminderRequest();
    String email = "planner@gmail.com";
    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    doNothing().when(reminderService).saveReminderByEntity(any());
    //when
    reminderApplication.createReminderByRequestAndEmail(request, email);

    //then
    verify(memberService, timeout(1)).getMemberByEmail(anyString());
    verify(reminderService, timeout(1)).saveReminderByEntity(any());
  }

  @Test
  @DisplayName("email을 통해 검증을 하고 id를 통해 삭제하는 로직 - 성공[planner도 검증을 거침]")
  void deleteReminderByIdAndEmail_success_planner() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(reminderService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(true);
    doNothing().when(reminderService).deleteReminderById(anyLong());
    when(plannerService.existByStudyIdAndType(anyLong(),any()))
        .thenReturn(true);
    doNothing().when(plannerService).deletePlanner(anyLong(),any());
    //when
    reminderApplication.deleteReminderByIdAndEmail(id,email);

    //then
    verify(reminderService, timeout(1)).existsByIdAndEmail(anyLong(), anyString());
    verify(reminderService, timeout(1)).deleteReminderById(anyLong());
    verify(plannerService, timeout(1)).existByStudyIdAndType(anyLong(),any());
    verify(plannerService, timeout(1)).deletePlanner(anyLong(),any());
  }

  @Test
  @DisplayName("email을 통해 검증을 하고 id를 통해 삭제하는 로직 - 성공")
  void deleteReminderByIdAndEmail_success() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(reminderService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(true);
    doNothing().when(reminderService).deleteReminderById(anyLong());
    when(plannerService.existByStudyIdAndType(anyLong(),any()))
        .thenReturn(false);
    //when
    reminderApplication.deleteReminderByIdAndEmail(id,email);

    //then
    verify(reminderService, timeout(1)).existsByIdAndEmail(anyLong(), anyString());
    verify(reminderService, timeout(1)).deleteReminderById(anyLong());
    verify(plannerService, timeout(1)).existByStudyIdAndType(anyLong(),any());
    verify(plannerService, never()).deletePlanner(anyLong(),any());
  }
  @Test
  @DisplayName("email을 통해 검증을 하고 id를 통해 삭제하는 로직 - 실패[empty]")
  void deleteReminderByIdAndEmail_fail() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(reminderService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(false);

    //when
    SuperException result = assertThrows(
        ExceptionNotFoundReminder.class,
        () -> reminderApplication.deleteReminderByIdAndEmail(id,email));

    //then
    assertEquals(result.getMessage(), "존재하지 않는 리마인드입니다.");
    verify(reminderService, timeout(1)).existsByIdAndEmail(anyLong(), anyString());
    verify(reminderService, never()).deleteReminderById(anyLong());
    verify(plannerService, never()).existByStudyIdAndType(anyLong(),any());
    verify(plannerService, never()).deletePlanner(anyLong(),any());
  }
  @Test
  @DisplayName("id, 이메일을 통환 완료 로직 - 실패[empty]")
  void completeByIdAndEmail_fail() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(reminderService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(false);

    //when
    SuperException result = assertThrows(
        ExceptionNotFoundReminder.class,
        () -> reminderApplication.completeByIdAndEmail(id,email));

    //then
    assertEquals(result.getMessage(), "존재하지 않는 리마인드입니다.");
    verify(reminderService, timeout(1)).existsByIdAndEmail(anyLong(), anyString());
    verify(reminderService, never()).getReminderById(anyLong());
  }

  @Test
  @DisplayName("id, 이메일을 통환 완료 로직 - 성공")
  void completeByIdAndEmail_success() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";
    Reminder reminder = CommonHelper.createReminder();
    when(reminderService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(true);
    when(reminderService.getReminderById(anyLong()))
        .thenReturn(reminder);
    //when
    reminderApplication.completeByIdAndEmail(id,email);

    //then
    verify(reminderService, timeout(1)).existsByIdAndEmail(anyLong(), anyString());
    verify(reminderService, timeout(1)).getReminderById(anyLong());
  }

  @Test
  @DisplayName("id, 이메일을 통환 완료 로직 - 실패[이미 완료]")
  void completeByIdAndEmail_fail_complete() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";
    Reminder reminder = CommonHelper.createReminderByCompleteTrue();
    when(reminderService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(true);
    when(reminderService.getReminderById(anyLong()))
        .thenReturn(reminder);
    //when
    SuperException result = assertThrows(
        ExceptionCompleteReminder.class,
        () -> reminderApplication.completeByIdAndEmail(id,email)
    );

    //then
    assertEquals(result.getMessage(),"이미 완료한 리마인드입니다.");
    verify(reminderService, timeout(1)).existsByIdAndEmail(anyLong(), anyString());
    verify(reminderService, timeout(1)).getReminderById(anyLong());
  }

  @Test
  @DisplayName("id, email을 통한 조회 로직 - 성공")
  void getByIdAndEmail(){
    //given
    Long id = 1L;
    String email = "planner@gmail.com";
    Reminder reminder = CommonHelper.createReminder();
    Study study = CommonHelper.createStudy();

    when(reminderService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(true);
    when(reminderService.getReminderById(anyLong()))
        .thenReturn(reminder);
    when(studyService.getStudyById(anyLong()))
        .thenReturn(study);
    //when
    ReminderDto result = reminderApplication.getByIdAndEmail(id, email);

    //then
    assertEquals(result.getReminderId(),reminder.getReminderId());
    assertEquals(result.getReminderTypeId(),reminder.getReminderTypeId());
    assertEquals(result.getTitle(),study.getStudyTitle());
    assertEquals(result.getContent(),study.getStudyContent());
    assertEquals(result.getReminderType(),reminder.getReminderType());
  }

  @Test
  @DisplayName("id, email을 통한 여러 건 조회 로직 - 성공")
  void getListByEmail(){
    //given
    Study study = CommonHelper.createStudy();
    Page<Reminder> reminderPage = new PageImpl<>(CommonHelper.createRemindersByNoId());
    when(reminderService.getListByEmailAndPage(anyString(), any()))
        .thenReturn(reminderPage);
    when(studyService.getStudyById(anyLong()))
        .thenReturn(study);
    //when
    Page<ReminderDto> reminderDtoPage = reminderApplication.getListByEmail(anyString(), any());

    //then
    List<ReminderDto> result = reminderDtoPage.getContent();
    List<Reminder> reminders = reminderPage.getContent();
    for (int i = 0; i < reminderPage.getSize(); i++) {
      assertEquals(result.get(i).getReminderId(), reminders.get(i).getReminderId());
      assertEquals(result.get(i).getReminderTypeId(), reminders.get(i).getReminderTypeId());
      assertEquals(result.get(i).getTitle(), study.getStudyTitle());
      assertEquals(result.get(i).getContent(), study.getStudyContent());
      assertEquals(result.get(i).getReminderType(), reminders.get(i).getReminderType());
    }
  }
}