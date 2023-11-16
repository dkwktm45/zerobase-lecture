package com.project.lecture.repository;

import com.project.lecture.entity.Planner;
import com.project.lecture.type.StudyType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

  @Modifying
  @Query(value = "delete from Planner p "
      + "where p.plannerType = '1' "
      + "and p.plannerTypeId in :lecturesIdx "
      + "and p.member.memberId = :memberId")
  void deleteLecturesById(@Param("lecturesIdx") List<Long> lecturesIdx, Long memberId);

  Optional<Planner> findByAndPlannerTypeIdAndPlannerType(Long studyId, StudyType studyType);

  boolean existsByPlannerIdAndMember_Email(Long id, String email);

  @Query(value = "select * from Planner p "
      + "JOIN Member m ON p.memberId = m.memberId " +
      "WHERE p.plannerDt BETWEEN :startDate AND :endDate " +
      "AND m.email = :email " +
      "AND p.plannerComplete = :flag " +
      "ORDER BY p.plannerDt",nativeQuery = true)
  List<Planner> findByPlannersByNotComplete(LocalDate startDate, LocalDate endDate, String email, boolean flag);
}
