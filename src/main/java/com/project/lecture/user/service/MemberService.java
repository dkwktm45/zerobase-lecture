package com.project.lecture.user.service;

import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.ExceptionNotFoundUser;
import com.project.lecture.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;


  public boolean hasEmail(String email) {
    return memberRepository.existsByEmail(email);
  }

  public void createUser(Member entity) {
    memberRepository.save(entity);
  }

  public Member getMemberByEmail(String email) {
    return memberRepository.findByEmail(email)
        .orElseThrow(ExceptionNotFoundUser::new);
  }
}
