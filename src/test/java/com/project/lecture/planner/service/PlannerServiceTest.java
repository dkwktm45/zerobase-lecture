package com.project.lecture.planner.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Planner;
import com.project.lecture.repository.PlannerRepository;
import com.project.lecture.type.StudyType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlannerServiceTest {

  @Mock
  private PlannerRepository plannerRepository;

  @Mock
  private PlannerService mockPlannerService;

  @InjectMocks
  private PlannerService plannerService;
  @BeforeEach
  void setUp() {
    mockPlannerService = Mockito.mock(PlannerService.class);
  }
  @Test
  @DisplayName("플레너 테이블에서 타입에 맞는 컬럼 삭제")
  void deletePlanner() {
    Long id = 1L;
    StudyType studyType = StudyType.LECTURE;

    // given
    doNothing().when(plannerRepository)
        .deleteByPlannerTypeAndPlannerTypeId(any(), anyLong());
    // when
    plannerService.deletePlanner(id,studyType);

    // then
    verify(plannerRepository,timeout(1))
        .deleteByPlannerTypeAndPlannerTypeId(any(),anyLong());
  }

  @Test
  @DisplayName("플래너 테이블에서 강좌에 맞는 컬럼을 삭제")
  void existCourseByPlanner() {
    // given
    Long courseId = 1L;
    List<Planner> planners = CommonHelper.createPlannersAndCourseForm();
    // when
    plannerService.existCourseByPlanner(courseId,planners);

    // then
    verify(plannerRepository, times(1))
        .deleteByPlannerTypeAndPlannerTypeId(eq(StudyType.COURSE), eq(courseId));
  }

  @Test
  @DisplayName("플래너 테이블에서 강좌에 맞는 컬럼을 삭제 - 컬럼이 없는 경우")
  void existCourseByPlanner_null() {
    // given
    Long courseId = 10L;
    List<Planner> planners = CommonHelper.createPlannersAndCourseForm();
    // when
    plannerService.existCourseByPlanner(courseId,planners);

    // then
    verify(plannerRepository, never())
        .deleteByPlannerTypeAndPlannerTypeId(any(), any());
  }

  @Test
  @DisplayName("플래너 테이블에서 강의에 맞는 컬럼을 삭제")
  void existLectureByPlanner() {
    // given
    List<Lecture> lectures = CommonHelper.createLecturesForm();
    List<Planner> planners = CommonHelper.createPlannersAndLectureForm();
    // when
    plannerService.existLectureByPlanner(planners,lectures);

    // then
    verify(plannerRepository, times(2))
        .deleteByPlannerTypeAndPlannerTypeId(eq(StudyType.LECTURE), anyLong());
  }

  @Test
  @DisplayName("플래너 테이블에서 강의에 맞는 컬럼을 삭제")
  void existLectureByPlanner_null() {
    // given
    List<Lecture> lectures = CommonHelper.createLecturesForm();
    List<Planner> planners = CommonHelper.createPlannersAndCourseForm();
    // when
    plannerService.existLectureByPlanner(planners,lectures);

    // then
    verify(plannerRepository, never())
        .deleteByPlannerTypeAndPlannerTypeId(any(), anyLong());
  }
}