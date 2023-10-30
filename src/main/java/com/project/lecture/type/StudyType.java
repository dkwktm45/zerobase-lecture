package com.project.lecture.type;

import com.project.lecture.exception.kind.ExceptionNotValidEnum;
import com.project.lecture.type.converter.legacy.LegacyCodeCommonType;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum StudyType implements LegacyCodeCommonType {
  LECTURE("1","강좌"),REMINDER("2","리마인더"),STUDY("3","공부"),COURSE("4","강의");

  private final String code;
  private final String description;

  StudyType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static StudyType ofCode(String code){
    return Arrays.stream(StudyType.values())
        .filter(e -> e.getCode().equals(code))
        .findAny()
        .orElseThrow(() -> new ExceptionNotValidEnum());
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
