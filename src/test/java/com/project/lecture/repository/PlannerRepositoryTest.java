package com.project.lecture.repository;

import org.awaitility.Awaitility;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Planner;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PlannerRepositoryTest {

  @Autowired
  private PlannerRepository plannerRepository;
  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("planner_type 과 planner_type_id에 맞는 컬럼을 삭제한다.")
  void deleteByPlannerTypeAndPlannerTypeId() {
    // given
    Planner planner = CommonHelper.createPlannerForm();
    plannerRepository.save(planner);

    // when
    plannerRepository.deleteByPlannerTypeAndPlannerTypeId(
        planner.getPlannerType(), planner.getPlannerTypeId()
    );

    Long count = plannerRepository.count();
    assertEquals(count, 0);
  }

  @Test
  @DisplayName("planner_type 과 planner_type_id에 맞는 컬럼 여러 개를 삭제한다. - lecture")
  void deleteLecturesById(){
    // given
    List<Planner> planners = CommonHelper.createPlannersForm();
    memberRepository.save(CommonHelper.createOriginMemberForm());
    plannerRepository.saveAll(planners);
    List<Long> longs = planners.stream().map(Planner::getPlannerTypeId)
        .collect(Collectors.toList());

    // when
    plannerRepository.deleteLecturesById(
        longs, planners.get(0).getMember().getMemberId()
    );

    // then
    Awaitility.await().atMost(100, SECONDS).until(() -> {
      long count = plannerRepository.count();
      return count == 0;
    });

    Long count = plannerRepository.count();
    assertEquals(count, 0);
  }
}