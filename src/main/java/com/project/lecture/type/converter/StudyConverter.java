package com.project.lecture.type.converter;

import com.project.lecture.type.StudyType;
import javax.persistence.Converter;

@Converter
public class StudyConverter extends AbstractLegacyEnumAttributeConverter<StudyType>{

  public static final String ENUM_NAME = "스터디 타입";
  public StudyConverter() {
    super(StudyType.class, ENUM_NAME);
  }
}
