package com.project.lecture.repository;

import com.project.lecture.entity.MemberCourseLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCourseRepository extends JpaRepository<MemberCourseLecture,Long> {

}
