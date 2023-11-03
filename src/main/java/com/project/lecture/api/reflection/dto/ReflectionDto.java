package com.project.lecture.api.reflection.dto;

import com.project.lecture.entity.Reflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReflectionDto {
  private Long reflectionId;
  private String reflectionTitle;
  private String reflectionContent;
  private boolean reflectionComplete;
  private String weekDt;

  public static ReflectionDto toDto(Reflection reflection){
    return ReflectionDto.builder()
        .reflectionComplete(reflection.isReflectionComplete())
        .reflectionContent(reflection.getReflectionContent())
        .reflectionTitle(reflection.getReflectionTitle())
        .reflectionId(reflection.getReflectionId())
        .weekDt(reflection.getWeekDt())
        .build();
  }
}
