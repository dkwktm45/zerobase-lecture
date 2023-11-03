package com.project.lecture.api.reflection.application;

import com.project.lecture.api.reflection.dto.ReflectionRequest.Create;
import com.project.lecture.api.reflection.service.ReflectionService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reflection;
import com.project.lecture.exception.kind.ExceptionNotFoundReflection;
import com.project.lecture.exception.kind.ExceptionNotValidWeekDt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReflectionApplication {

  private final MemberService memberService;
  private final ReflectionService reflectionService;
  public void createReflectionByDtoAndEmail(Create request, String email) {
    if (!request.validWeek()){
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
    if (!reflectionService.existsByIdAndEmail(id,email)){
      throw new ExceptionNotFoundReflection();
    }

    reflectionService.deleteReflectionById(id);
  }
}
