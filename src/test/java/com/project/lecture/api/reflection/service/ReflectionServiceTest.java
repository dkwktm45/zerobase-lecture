package com.project.lecture.api.reflection.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Reflection;
import com.project.lecture.exception.SuperException;
import com.project.lecture.exception.kind.ExceptionNotFoundReflection;
import com.project.lecture.repository.ReflectionRepository;
import java.util.Optional;
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

  @Test
  @DisplayName("id를 통한 Reflection 가져오기 - 성공")
  void getReflectionById_success() {
    //given
    Long id = 1L;
    Reflection reflection = CommonHelper.createReflectionBy();
    when(reflectionRepository.findById(id))
        .thenReturn(Optional.of(reflection));

    //when
    Reflection result = reflectionService.getReflectionById(id);

    //then
    assertEquals(reflection.getReflectionId(), result.getReflectionId());
    assertEquals(reflection.getReflectionTitle(), result.getReflectionTitle());
    assertEquals(reflection.getReflectionContent(), result.getReflectionContent());
    assertEquals(reflection.isReflectionComplete(), result.isReflectionComplete());
    verify(reflectionRepository, timeout(1)).findById(id);
  }

  @Test
  @DisplayName("id를 통한 Reflection 가져오기 - 실패")
  void getReflectionById_fail() {
    //given
    Long id = 1L;
    when(reflectionRepository.findById(id))
        .thenReturn(Optional.empty());

    //when
    SuperException result = assertThrows(
        ExceptionNotFoundReflection.class,
        () -> reflectionService.getReflectionById(id)
    );
    //then
    assertEquals("존재하지 않는 회고입니다.", result.getMessage());
    verify(reflectionRepository, timeout(1)).findById(id);
  }
}