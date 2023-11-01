package com.project.lecture.api.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudyDto {
  private long id;
  private String studyTitle;
  private String studyContent;
}
