package com.project.lecture.redis.dto;

import lombok.Getter;

@Getter
public enum Tier {
  SILVER(10 * 60, "SILVER"),
  GOLD(20 * 60, "GOLD"),
  BRONZE(30 * 60, "BRONZE"),
  PLATINUM(40 * 60, "PLATINUM");

  private final String  name;
  private final int time;

  Tier(int time, String name) {
    this.time = time;
    this.name = name;
  }
}
