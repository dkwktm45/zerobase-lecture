package com.project.lecture.redis.dto;

import static com.project.lecture.redis.dto.Tier.BRONZE;
import static com.project.lecture.redis.dto.Tier.GOLD;
import static com.project.lecture.redis.dto.Tier.PLATINUM;
import static com.project.lecture.redis.dto.Tier.SILVER;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserTier {

  private long time;
  private Tier tier;
  public UserTier(long time) {
    this.time = time;
    this.tier = inputTier(time);
  }

  public void plusTime(long time) {
    this.time += time;
    this.tier = inputTier(this.time);
  }

  private Tier inputTier(long time) {
    if (time < BRONZE.getTime()) {
      return BRONZE;
    } else if (time < SILVER.getTime()) {
      return SILVER;
    } else if (time < GOLD.getTime()) {
      return GOLD;
    } else {
      return PLATINUM;
    }
  }
}
