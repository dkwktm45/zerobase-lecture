package com.project.lecture.type;

import com.project.lecture.type.converter.legacy.LegacyCodeCommonType;

public enum SocialType implements LegacyCodeCommonType {
  KAKAO("0","KAKAO"), NAVER("1","NAVER"), GOOGLE("2", "GOOGLE");

  private final String code;
  private final String description;

  SocialType(String code, String description) {
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
