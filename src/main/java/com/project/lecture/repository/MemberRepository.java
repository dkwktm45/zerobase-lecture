package com.project.lecture.repository;

import com.project.lecture.entity.Member;
import com.project.lecture.type.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

  Optional<Member> findByEmail(String email);
  boolean existsByEmail(String email);

  Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String id);
}
