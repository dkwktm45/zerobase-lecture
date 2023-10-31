package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Planner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PlannerRepositoryTest {

  @Autowired
  private PlannerRepository plannerRepository;

  @Test
  @DisplayName("planner_type 과 planner_type_id에 맞는 컬럼을 삭제한다.")
  void deleteByPlannerTypeAndPlannerTypeId() {
    // given
    Planner planner = CommonHelper.createPlannerForm();
    plannerRepository.save(planner);

    // when
    plannerRepository.deleteByPlannerTypeAndPlannerTypeId(
        planner.getPlannerType(),planner.getPlannerTypeId()
    );

    Long count = plannerRepository.count();
    assertEquals(count,0);
  }
}