package com.project.lecture.planner.service;

import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Planner;
import com.project.lecture.repository.PlannerRepository;
import com.project.lecture.type.StudyType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlannerService {

  private final PlannerRepository plannerRepository;


  public void deletePlanner(Long id, StudyType studyType) {
    plannerRepository.deleteByPlannerTypeAndPlannerTypeId(studyType, id);
  }

  public void existCourseByPlanner(Long courseId, List<Planner> planners) {
    Planner infoByPlanner = planners.stream()
        .filter(i ->
            i.getPlannerTypeId().equals(courseId) &&
                i.getPlannerType().equals(StudyType.COURSE)
        ).findFirst().orElse(null);

    // todo 추후 정말 하나의 값만 있는지를 검토 : DB 데이터가 여러개 있을경우가 있는지
    if (!Objects.isNull(infoByPlanner)) {
      deletePlanner(infoByPlanner.getPlannerTypeId(), infoByPlanner.getPlannerType());
    }
  }

  public void existLectureByPlanner(List<Planner> planners, List<Lecture> lectures) {

    Map<Long, Planner> plannerMap = new HashMap<>();
    for (Planner planner : planners) {
      plannerMap.put(planner.getPlannerTypeId(), planner);
    }

    for (Lecture lecture : lectures) {
      Planner planner = plannerMap.get(lecture.getLectureId());
      if (planner != null && planner.getPlannerType().equals(StudyType.LECTURE)) {
        deletePlanner(lecture.getLectureId(), StudyType.LECTURE);
      }
    }
  }
}
