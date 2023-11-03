package com.project.lecture.api.reflection.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.api.reflection.dto.ReflectionRequest.Create;
import com.project.lecture.api.reflection.service.ReflectionService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionNotFoundReflection;
import com.project.lecture.exception.kind.ExceptionNotValidWeekDt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReflectionApplicationTest {

  @Mock
  private MemberService memberService;
  @Mock
  private ReflectionService reflectionService;
  @InjectMocks
  private ReflectionApplication reflectionApplication;

  @Test
  @DisplayName("회고 작성시 주간 정보가 옳바른지 확인하고 저장을 수행하는 로직 - 성공")
  void createReflectionByDtoAndEmail_success() {
    //given
    Create request = CommonHelper.reflectionRequestDto();
    Member member = CommonHelper.createMemberForm();

    when(memberService.getMemberByEmail(anyString()))
        .thenReturn(member);
    doNothing().when(reflectionService)
        .createByEntity(any());
    //when
    reflectionApplication.createReflectionByDtoAndEmail(request, member.getEmail());

    //then
    verify(memberService, timeout(1)).getMemberByEmail(anyString());
    verify(reflectionService, timeout(1)).createByEntity(any());
  }

  @Test
  @DisplayName("회고 작성시 주간 정보가 옳바른지 확인하고 저장을 수행하는 로직 - 실패[date 불일치]")
  void createReflectionByDtoAndEmail_fail() {
    //given
    Create request = CommonHelper.notValidReflectionRequestDto();

    //when
    SuperException result = assertThrows(ExceptionNotValidWeekDt.class,
        () -> reflectionApplication.createReflectionByDtoAndEmail(request, anyString()));

    //then
    assertEquals(result.getMessage(), "옳바르지 않는 주간 날짜입니다.");
    verify(memberService, never()).getMemberByEmail(anyString());
    verify(reflectionService, never()).createByEntity(any());
  }

  @Test
  @DisplayName("존재하는 회고 삭제 로직 - 성공")
  void deleteReflectionByIdAndEmail_success() {
    //given
    Long id = 1L;
    String email = "planner@email.com";

    when(reflectionService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(true);
    doNothing().when(reflectionService).deleteReflectionById(anyLong());

    //when
    reflectionApplication.deleteReflectionByIdAndEmail(id,email);

    //then
    verify(reflectionService, timeout(1)).existsByIdAndEmail(anyLong(), anyString());
    verify(reflectionService, timeout(1)).deleteReflectionById(anyLong());
  }
  @Test
  @DisplayName("존재하는 회고 삭제 로직 - 실패")
  void deleteReflectionByIdAndEmail_fail() {
    //given
    Long id = 1L;
    String email = "planner@email.com";

    when(reflectionService.existsByIdAndEmail(anyLong(), anyString()))
        .thenReturn(false);

    //when
    SuperException result = assertThrows(
        ExceptionNotFoundReflection.class,
        () -> reflectionApplication.deleteReflectionByIdAndEmail(id,email)
    );

    //then
    assertEquals(result.getMessage(),"존재하지 않는 회고입니다.");
    verify(reflectionService, timeout(1)).existsByIdAndEmail(anyLong(), anyString());
    verify(reflectionService, never()).deleteReflectionById(anyLong());
  }
}