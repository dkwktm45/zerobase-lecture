package com.project.lecture.type.converter;

import com.project.lecture.type.SocialType;
import javax.persistence.Converter;

@Converter
public class SocialConverter  extends AbstractLegacyEnumAttributeConverter<SocialType>{

  private static final String ENUM_NAME = "SNS 종류";

  public SocialConverter() {
    super(SocialType.class, ENUM_NAME);
  }
}
