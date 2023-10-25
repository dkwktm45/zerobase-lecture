package com.project.lecture.type;

import lombok.Getter;

@Getter
public enum AuthType {
  ADMIN("ROLE"),USER("USER"), GUEST("GUEST");
  private String description;
  AuthType(String description) {
    this.description = description;
  }
}
