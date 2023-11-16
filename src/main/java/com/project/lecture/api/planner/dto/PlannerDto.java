package com.project.lecture.api.planner.dto;

import com.project.lecture.entity.Planner;
import com.project.lecture.type.StudyType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerDto {
  private Long plannerId;
  private Long plannerTypeId;
  private StudyType plannerType;
  private LocalDate plannerDt;

  public static PlannerDto toDto(Planner planner){
    return PlannerDto.builder()
        .plannerDt(planner.getPlannerDt())
        .plannerId(planner.getPlannerId())
        .plannerTypeId(planner.getPlannerTypeId())
        .plannerType(planner.getPlannerType())
        .plannerDt(planner.getPlannerDt())
        .build();
  }
}
