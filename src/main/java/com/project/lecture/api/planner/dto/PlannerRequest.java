package com.project.lecture.api.planner.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PlannerRequest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Update{
    @NotNull(message = "빈 값은 들어올 수 없습니다.")
    private Long plannerId;
    @NotNull(message = "빈 값은 들어올 수 없습니다.")
    private LocalDate plannerDt;
  }

}
