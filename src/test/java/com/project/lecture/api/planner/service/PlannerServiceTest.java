package com.project.lecture.api.planner.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Planner;
import com.project.lecture.repository.PlannerRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlannerServiceTest {

  @Mock
  private PlannerRepository plannerRepository;

  @InjectMocks
  private PlannerService plannerService;

  @Test
  @DisplayName("플레너 테이블에서 타입에 맞는 컬럼 삭제")
  void deletePlanner() {
    Planner planner = CommonHelper.createPlannerByStudyForm();
    // given
    doNothing().when(plannerRepository)
        .delete(any());
    // when
    plannerService.deletePlanner(planner);

    // then
    verify(plannerRepository, timeout(1))
        .delete(any());
  }

  @Test
  @DisplayName("플래너 테이블에서 강좌에 맞는 컬럼을 삭제")
  void existCourseByPlanner() {
    // given
    Long courseId = 1L;
    List<Planner> planners = CommonHelper.createPlannersAndCourseForm();
    doNothing().when(plannerRepository).delete(any());

    // when
    plannerService.deleteIfExistCourse(courseId, planners);

    // then
    verify(plannerRepository, times(1))
        .delete(any());
  }

  @Test
  @DisplayName("플래너 테이블에서 강좌에 맞는 컬럼을 삭제 - 컬럼이 없는 경우")
  void existCourseByPlanner_null() {
    // given
    Long courseId = 10L;
    List<Planner> planners = CommonHelper.createPlannersAndCourseForm();
    // when
    plannerService.deleteIfExistCourse(courseId, planners);

    // then
    verify(plannerRepository, never())
        .delete(any());
  }

  @Test
  @DisplayName("플래너 테이블에서 강의에 맞는 컬럼을 삭제")
  void existLectureByPlanner() {
    // given
    List<Lecture> lectures = CommonHelper.createLecturesForm();
    Long memberId = 1L;
    // when
    plannerService.deleteIfExistLecture(memberId, lectures);

    // then
    verify(plannerRepository, times(1))
        .deleteLecturesById(anyList(), anyLong());
  }
}