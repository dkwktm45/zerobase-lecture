package com.project.lecture.repository;

import com.project.lecture.entity.Planner;
import com.project.lecture.type.StudyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlannerRepository extends JpaRepository<Planner,Long> {
  void deleteByPlannerTypeAndPlannerTypeId(StudyType studyType, Long id);
}
