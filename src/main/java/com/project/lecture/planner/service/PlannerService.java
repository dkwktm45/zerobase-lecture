package com.project.lecture.planner.service;

import com.project.lecture.repository.PlannerRepository;
import com.project.lecture.type.StudyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerService {

  private final PlannerRepository plannerRepository;


  public void deletePlanner(Long id, StudyType studyType) {
    plannerRepository.deleteByPlannerTypeAndPlannerTypeId(studyType, id);
  }
}
