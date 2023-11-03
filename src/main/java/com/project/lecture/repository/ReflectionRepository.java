package com.project.lecture.repository;

import com.project.lecture.entity.Reflection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReflectionRepository extends JpaRepository<Reflection,Long> {
  boolean existsByReflectionIdAndMember_Email(Long studyId, String email);

}
