package com.project.lecture.repository;

import com.project.lecture.entity.Member;
import com.project.lecture.entity.Study;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study,Long> {

  boolean existsByStudyIdAndMember_Email(Long studyId, String email);

  Page<Study> findByMember_Email(String email, Pageable pageable);

  List<Study> findAllByMemberAndStudyComplete(Member member, boolean completeFlag);
}
