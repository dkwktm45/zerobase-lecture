package com.project.lecture.type;

import lombok.Getter;

@Getter
public enum AuthType {
  ADMIN("ROLE_ADMIN"),USER("USER"), GUEST("GUEST");
  private String description;
  AuthType(String description) {
    this.description = description;
  }
}
