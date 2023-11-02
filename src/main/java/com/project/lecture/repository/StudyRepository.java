package com.project.lecture.repository;

import com.project.lecture.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study,Long> {

  boolean existsByStudyIdAndMember_Email(Long studyId, String email);
}
