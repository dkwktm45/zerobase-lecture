package com.project.lecture.type.converter.legacy;

import com.project.lecture.exception.kind.ExceptionNotValidEnum;
import java.util.EnumSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LegacyCodeEnumValueConverterUtils {

  public static <T extends Enum<T> & LegacyCodeCommonType> T ofLegacyCode(Class<T> enumClass,
      String legacyCode) {

    if (!StringUtils.hasText(legacyCode)) {
      return null;
    }

    return EnumSet.allOf(enumClass)
        .stream()
        .filter(e -> e.getLegacyCode().equals(legacyCode))
        .findAny()
        .orElseThrow(() -> new ExceptionNotValidEnum(enumClass.getName()));
  }

  public static <T extends Enum<T> & LegacyCodeCommonType> String toLegacyCode(T enumValue) {
    return enumValue.getLegacyCode();
  }
}
