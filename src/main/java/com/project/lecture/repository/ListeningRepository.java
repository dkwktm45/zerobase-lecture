package com.project.lecture.repository;

import com.project.lecture.entity.Listening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ListeningRepository extends JpaRepository<Listening,Long> {

  @Query(value = "select exists ("
      + "select 1 from listening "
      + "where member_id = :memberId "
      + "and course_id = :courseId)" , nativeQuery = true)
  int existsByMemberIdAndCourseId(Long memberId, Long courseId);
}
