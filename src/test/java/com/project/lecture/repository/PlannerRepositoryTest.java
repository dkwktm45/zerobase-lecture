package com.project.lecture.repository;

import com.project.lecture.type.StudyType;
import org.awaitility.Awaitility;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Planner;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension.class)
class PlannerRepositoryTest {

  @Autowired
  private PlannerRepository plannerRepository;
  @Autowired
  private MemberRepository memberRepository;


  @Test
  @DisplayName("planner_type 과 planner_type_id에 맞는 컬럼을 삭제한다.")
  void deleteByPlannerTypeAndPlannerTypeId() {
    // given
    Planner planner = CommonHelper.createPlannerByCourseForm();
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
    plannerRepository.saveAllAndFlush(planners);
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

  @Test
  @DisplayName("개인학습 타입과 id가 일치하는 컬럼 삭제 - true")
  void existsByStudyId_true(){
    //given
    Planner planner = CommonHelper.createPlannerByStudyForm();
    plannerRepository.save(planner);
    //when
    boolean result = plannerRepository
        .existsByPlannerTypeIdAndPlannerType(1L, StudyType.STUDY);

    //then
    assertTrue(result);
  }
  @Test
  @DisplayName("개인학습 타입과 id가 일치하는 컬럼 삭제 - false")
  void existsByStudyId_false(){
    //given
    //when
    boolean result = plannerRepository
        .existsByPlannerTypeIdAndPlannerType(1L, StudyType.STUDY);

    //then
    assertFalse(result);
  }
}