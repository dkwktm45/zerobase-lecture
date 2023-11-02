package com.project.lecture.repository;

import com.project.lecture.entity.Planner;
import com.project.lecture.type.StudyType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlannerRepository extends JpaRepository<Planner,Long> {
  void deleteByPlannerTypeAndPlannerTypeId(StudyType studyType, Long id);

  @Modifying
  @Query(value = "delete from Planner p "
      + "where p.plannerType = '1' "
      + "and p.plannerTypeId in :lecturesIdx "
      + "and p.member.memberId = :memberId")
  void deleteLecturesById(@Param("lecturesIdx") List<Long> lecturesIdx, Long memberId);
  boolean existsByPlannerTypeIdAndPlannerType(Long studyId,StudyType studyType);
}
