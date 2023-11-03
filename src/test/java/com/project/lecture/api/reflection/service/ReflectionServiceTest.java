package com.project.lecture.api.reflection.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Reflection;
import com.project.lecture.repository.ReflectionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReflectionServiceTest {

  @Mock
  private ReflectionRepository reflectionRepository;
  @InjectMocks
  private ReflectionService reflectionService;

  @Test
  @DisplayName("이메일과 아이디를 통한 값이 있는지 확인하는 로직 - true")
  void existsByIdAndEmail_true() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(reflectionRepository.existsByReflectionIdAndMember_Email(id, email))
        .thenReturn(true);
    //when
    boolean result = reflectionService.existsByIdAndEmail(id, email);

    //then
    assertTrue(result);
  }

  @Test
  @DisplayName("이메일과 아이디를 통한 값이 있는지 확인하는 로직 - false")
  void existsByIdAndEmail_false() {
    //given
    Long id = 1L;
    String email = "planner@gmail.com";

    when(reflectionRepository.existsByReflectionIdAndMember_Email(id, email))
        .thenReturn(false);
    //when
    boolean result = reflectionService.existsByIdAndEmail(id, email);

    //then
    assertFalse(result);
  }

  @Test
  @DisplayName("회고 저장")
  void createByEntity() {
    //given
    Reflection reflection = CommonHelper.createReflectionByNoId();

    when(reflectionRepository.save(reflection))
        .thenReturn(reflection);

    //when
    reflectionService.createByEntity(reflection);

    //then
    verify(reflectionRepository, timeout(1)).save(reflection);
  }

  @Test
  @DisplayName("id를 통한 회고 삭제")
  void deleteById() {
    //given
    Long id = 1L;

    doNothing().when(reflectionRepository).deleteById(id);

    //when
    reflectionService.deleteReflectionById(id);

    //then
    verify(reflectionRepository, timeout(1)).deleteById(id);
  }
}