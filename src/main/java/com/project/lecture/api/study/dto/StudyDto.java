package com.project.lecture.api.study.dto;

import com.project.lecture.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudyDto {

  private long id;

  private String studyTitle;
  private String studyContent;
  private boolean studyComplete;
  private String createDt;

  public StudyDto(Study study) {
    this.id = study.getStudyId();
    this.studyTitle = study.getStudyTitle();
    this.studyContent = study.getStudyContent();
    this.studyComplete = study.isStudyComplete();
    this.createDt = study.getCreateDt();
  }
  public static Page<StudyDto> toDotList(Page<Study> studies){
    return studies.map(StudyDto::new);
  }
}
