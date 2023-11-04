package com.project.lecture.api.reflection.application;

import com.project.lecture.api.reflection.dto.ReflectionDto;
import com.project.lecture.api.reflection.dto.ReflectionRequest.Create;
import com.project.lecture.api.reflection.service.ReflectionService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reflection;
import com.project.lecture.exception.kind.ExceptionCompleteReflection;
import com.project.lecture.exception.kind.ExceptionNotFoundReflection;
import com.project.lecture.exception.kind.ExceptionNotValidWeekDt;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReflectionApplication {

  private final MemberService memberService;
  private final ReflectionService reflectionService;

  public void createReflectionByDtoAndEmail(Create request, String email) {
    if (!request.validWeek()) {
      throw new ExceptionNotValidWeekDt(request.getWeekDt());
    }

    Member member = memberService.getMemberByEmail(email);

    reflectionService.createByEntity(
        Reflection.builder()
            .reflectionTitle(request.getReflectionTitle())
            .reflectionContent(request.getReflectionContent())
            .weekDt(request.getWeekDt())
            .member(member)
            .build()
    );
  }

  public void deleteReflectionByIdAndEmail(Long id, String email) {
    if (!reflectionService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReflection();
    }

    reflectionService.deleteReflectionById(id);
  }

  public void completeByIdAndEmail(Long id, String email) {
    if (!reflectionService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReflection();
    }
    Reflection reflection = reflectionService.getReflectionById(id);

    if (reflection.isReflectionComplete()) {
      throw new ExceptionCompleteReflection();
    }

    reflection.changeCompleteIntoTrue();
  }

  public ReflectionDto getByIdAndEmail(Long id, String email) {
    if (!reflectionService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReflection();
    }

    return ReflectionDto.toDto(
        reflectionService.getReflectionById(id)
    );
  }

  public List<ReflectionDto> getListByEmail(String email) {
    Member member = memberService.getMemberByEmail(email);

    return member.getReflections()
        .stream()
        .map(ReflectionDto::toDto)
        .collect(Collectors.toList());
  }
}
