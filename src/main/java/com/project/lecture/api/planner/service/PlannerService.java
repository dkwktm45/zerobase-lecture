package com.project.lecture.api.planner.service;

import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Planner;
import com.project.lecture.repository.PlannerRepository;
import com.project.lecture.type.StudyType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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

  public void deleteIfExistCourse(Long courseId, List<Planner> planners) {
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

  public void deleteIfExistLecture(Long memberId, List<Lecture> lectures) {
    List<Long> lectureIdx = lectures.stream().map(Lecture::getLectureId)
        .collect(Collectors.toList());

    plannerRepository.deleteLecturesById(lectureIdx, memberId);
  }

  public boolean existStudyId(Long id) {
    return plannerRepository.existsByStudyId(id) >= 1;
  }
}
