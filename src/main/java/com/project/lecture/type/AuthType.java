package com.project.lecture.type;

import lombok.Getter;

@Getter
public enum AuthType {
  ADMIN("ADMIN"),USER("USER");
  private String description;
  AuthType(String description) {
    this.description = description;
  }
}
