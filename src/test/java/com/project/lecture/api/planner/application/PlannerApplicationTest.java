package com.project.lecture.api.planner.application;

import com.project.lecture.entity.Planner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlannerApplicationTest {

  @Test
  void getMonthlyPlannerByRequestAndEmail() {
    LocalDate currentDate = LocalDate.now();
    List<Planner> planners = new ArrayList<>();

    for (int i = 3; i >= 1; i--) {
      planners.add(
          Planner.builder()
              .plannerDt(LocalDate.now().minusDays(2)).build()
      );
    }
    for (int i = 3; i >= 0; i--) {
      planners.add(
          Planner.builder()
              .plannerDt(LocalDate.now().plusDays(i)).build()
      );
    }
    for (int i = 0; i < 3; i++) {
      planners.add(
          Planner.builder()
              .plannerDt(LocalDate.now().plusDays(4)).build()
      );
    }

    Collections.sort(planners, (a, b) -> a.getPlannerDt().compareTo(b.getPlannerDt()));
    long daysBetween = ChronoUnit.DAYS.between(currentDate, planners.get(0).getPlannerDt());

    if (daysBetween < 0) {
      planners.forEach(i -> System.out.println(i.getPlannerDt()));
      System.out.println("-------------------------------");
      // init
      LocalDate changeDt = LocalDate.now();
      LocalDate beforeDate = planners.get(0).getPlannerDt();
      planners.get(0).changeDate(changeDt);
      for (int i = 1; i < planners.size(); i++) {
        Planner planner = planners.get(i);
        Planner previousPlanner = planners.get(i - 1);
        // 같은 값인 경우
        if (planner.getPlannerDt().isEqual(beforeDate)) {
          planner.changeDate(changeDt);
        }
        // 현재 날짜보다 작거나 같은 경우는 or 이전 값이 현재 값보다 작을 경우
        else if (planner.getPlannerDt().compareTo(currentDate) <= 0 ||
            previousPlanner.getPlannerDt().isAfter(planner.getPlannerDt())) {
          changeDt = planners.get(i - 1).getPlannerDt().plusDays(1);
          beforeDate = planner.getPlannerDt();
          planner.changeDate(changeDt);
        }
        // 만약 이전 index 날짜와 현재 index 날짜가 같은 경우는 값을 1씩 증가하고 값을 갱신
        else if (previousPlanner.getPlannerDt().isEqual(planner.getPlannerDt())) {
          beforeDate = previousPlanner.getPlannerDt();
          changeDt = beforeDate.plusDays(1);
          planner.plusDay(1);
        } else {
          break;
        }
      }
    }
    planners.forEach(i -> System.out.println(i.getPlannerDt()));
  }
}