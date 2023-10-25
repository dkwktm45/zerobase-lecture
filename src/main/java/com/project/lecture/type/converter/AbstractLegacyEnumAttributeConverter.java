package com.project.lecture.type.converter;

import com.project.lecture.exception.kind.ExceptionNotValidEnum;
import com.project.lecture.type.converter.legacy.LegacyCodeCommonType;
import com.project.lecture.type.converter.legacy.LegacyCodeEnumValueConverterUtils;
import javax.persistence.AttributeConverter;
import lombok.Getter;

@Getter
public class AbstractLegacyEnumAttributeConverter<E extends Enum<E> & LegacyCodeCommonType> implements
    AttributeConverter<E, String> {

  public AbstractLegacyEnumAttributeConverter(Class<E> targetEnumClass, String enumName) {
    this.targetEnumClass = targetEnumClass;
    this.enumName = enumName;
  }

  private final Class<E> targetEnumClass;
  private final String enumName;
  @Override
  public String convertToDatabaseColumn(E attribute) {
    if (attribute == null) {
      throw new ExceptionNotValidEnum(enumName);
    }
    return LegacyCodeEnumValueConverterUtils.toLegacyCode(attribute);
  }

  @Override
  public E convertToEntityAttribute(String dbData) {
    return LegacyCodeEnumValueConverterUtils.ofLegacyCode(targetEnumClass,dbData);
  }
}
