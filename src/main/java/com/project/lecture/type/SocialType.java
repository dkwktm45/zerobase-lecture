package com.project.lecture.type;

import com.project.lecture.exception.kind.ExceptionNotValidEnum;
import com.project.lecture.type.converter.legacy.LegacyCodeCommonType;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum SocialType implements LegacyCodeCommonType {
  KAKAO("0","KAKAO"), NAVER("1","NAVER"), GOOGLE("2", "GOOGLE"),PLANNER("3","PLANNER");

  private final String code;
  private final String description;

  SocialType(String code, String description) {
    this.code = code;
    this.description = description;
  }
  public static SocialType ofCode(String code){
    return Arrays.stream(SocialType.values())
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
