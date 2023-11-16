package com.project.lecture.repository;

import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCourseRepository extends JpaRepository<MemberCourseLecture,Long> {

  boolean existsByCourseIdAndMember(Long courseId, Member member);

  Optional<MemberCourseLecture> findByMemberAndCourseId(Member member, Long courseId);
}
