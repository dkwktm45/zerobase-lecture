package com.project.lecture.type;

import com.project.lecture.type.converter.legacy.LegacyCodeCommonType;
import lombok.Getter;

@Getter
public enum StudyType implements LegacyCodeCommonType {
  LECTURE("1","강의"),REMINDER("2","리마인더"),STUDY("3","공부");

  private final String code;
  private final String description;

  StudyType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  @Override
  public String getLegacyCode() {
    return this.code;
  }

  @Override
  public String getDesc() {
    return this.description;
  }
}
