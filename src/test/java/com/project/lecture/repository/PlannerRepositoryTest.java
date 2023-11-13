package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Planner;
import com.project.lecture.type.StudyType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
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
    Planner request = plannerRepository.save(planner);

    // when
    plannerRepository.delete(
        request
    );

    Long count = plannerRepository.count();
    assertEquals(count, 0);
  }

  @Test
  @DisplayName("planner_type 과 planner_type_id에 맞는 컬럼 여러 개를 삭제한다. - lecture")
  void deleteLecturesById() {
    // given
    List<Planner> planners = CommonHelper.createPlannersForm();
    memberRepository.save(CommonHelper.createOriginMemberFormByNoId());
    plannerRepository.saveAllAndFlush(planners);
    List<Long> longs = planners.stream().map(Planner::getPlannerTypeId)
        .collect(Collectors.toList());

    // when
    plannerRepository.deleteLecturesById(
        longs, planners.get(0).getMember().getMemberId()
    );

    // then
    Long count = plannerRepository.count();
    assertEquals(count, 0);
  }

  @Test
  @DisplayName("개인학습 타입과 id가 일치하는 컬럼 삭제 - true")
  void existsByStudyId_true() {
    //given
    Planner planner = CommonHelper.createPlannerByStudyForm();
    plannerRepository.save(planner);
    //when
    Planner result = plannerRepository
        .findByAndPlannerTypeIdAndPlannerType(1L, StudyType.STUDY)
        .orElse(null);

    //then
    assertNotNull(result);
  }

  @Test
  @DisplayName("개인학습 타입과 id가 일치하는 컬럼 삭제 - false")
  void existsByStudyId_false() {
    //given
    //when
    Optional<Planner> result = plannerRepository
        .findByAndPlannerTypeIdAndPlannerType(1L, StudyType.STUDY);

    //then
    assertTrue(result.isEmpty());
  }

  @Test
  void findByPlannersByNotComplete() {
    //given
    String email = "planner@gmail.com";
    List<Planner> planners = CommonHelper.createPlannersForm();
    memberRepository.save(CommonHelper.createOriginMemberFormByNoId());
    plannerRepository.saveAllAndFlush(planners);
    LocalDate start = LocalDate.now();
    LocalDate end = start.plusDays(1);
    //when
    List<Planner> result = plannerRepository
        .findByPlannersByNotComplete(start, end, email, false);

    //then
    assertNotNull(result);
  }

  @Autowired
  private EntityManager em;

  @AfterEach
  void setUp() {
    em.createNativeQuery(
            "ALTER TABLE member ALTER COLUMN `memberId` RESTART                                                                     WITH 1")
        .executeUpdate();
  }
}