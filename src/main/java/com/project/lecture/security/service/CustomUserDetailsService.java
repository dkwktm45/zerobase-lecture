package com.project.lecture.security.service;

import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.ExceptionNotFoundUser;
import com.project.lecture.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;
  @Override
  public UserDetails loadUserByUsername(String userPhone) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(userPhone).orElseThrow(ExceptionNotFoundUser::new);

    return org.springframework.security.core.userdetails.User.builder()
        .username(member.getEmail())
        .password(member.getPassword())
        .roles(member.getAuthType())
        .build();
  }
}