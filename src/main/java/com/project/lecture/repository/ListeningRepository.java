package com.project.lecture.repository;

import com.project.lecture.entity.Listening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ListeningRepository extends JpaRepository<Listening,Long> {

  @Query(value = "select exists ("
      + "select 1 from listening "
      + "where member_id = :memberId "
      + "and course_id = :courseId)" , nativeQuery = true)
  boolean existsByMemberIdAndCourseId(Long memberId, Long courseId);

  @Query(value = "delete from listening "
      + "where member_id = :memberId "
      + "and course_id = :courseId " , nativeQuery = true)
  @Modifying(clearAutomatically = true)
  void deleteByMemberIdAndCourseId(Long memberId, Long courseId);

}
