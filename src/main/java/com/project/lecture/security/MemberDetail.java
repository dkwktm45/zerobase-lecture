package com.project.lecture.security;

import com.project.lecture.entity.Member;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class MemberDetail extends User {

  private final Member member;

  public MemberDetail(Member member) {
    super(member.getEmail()
        , member.getNickName()
        , Collections.singleton(new SimpleGrantedAuthority(member.getAuthType())));
    this.member = member;
  }

  @Override
  public String getUsername() {
    return member.getEmail();
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }
  @Override
  public boolean isEnabled() {
    return true;
  }
}
