package com.project.lecture.type;

import lombok.Getter;

@Getter
public enum UrlCheck {
  NO_CHECK_LOGIN("login"),NO_CHECK_JOIN("join");

  private final String url;
  UrlCheck(String url) {
    this.url = url;
  }
}
