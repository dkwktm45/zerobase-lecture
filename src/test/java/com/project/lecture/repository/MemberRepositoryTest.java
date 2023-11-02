package com.project.lecture.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.ExceptionNotFoundUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("이메일에 해당하는 회원")
  void findByEmail() {
    Member member = CommonHelper.createMemberFormByNoId();

    memberRepository.save(member);

    Member result = memberRepository.findByEmail(member.getEmail())
        .orElseThrow(ExceptionNotFoundUser::new);

    assertEquals(member.getNickName(), result.getNickName());
    assertEquals(member.getEmail(), result.getEmail());
    assertEquals(member.getPassword(), result.getPassword());
    assertEquals(member.getAuthType(), result.getAuthType());
    assertEquals(member.getSocialType(), result.getSocialType());
  }

  @Test
  @DisplayName("Member 테이블에 이메일이 있는지 확인하기 - true")
  void existsByEmail() {
    Member member = CommonHelper.createMemberFormByNoId();

    memberRepository.save(member);

    boolean result = memberRepository.existsByEmail(member.getEmail());

    assertTrue(result);
  }

  @Test
  @DisplayName("Member 테이블에 이메일이 있는지 확인하기 - false")
  void existsByEmail_false() {
    Member member = CommonHelper.createMemberFormByNoId();

    boolean result = memberRepository.existsByEmail(member.getEmail());

    assertFalse(result);
  }

  @Test
  @DisplayName("sns 타입 및 id가 있는 member를 가져온다.")
  void findBySocialTypeAndSocialId() {
    Member member = CommonHelper.createMemberFormByNoId();

    memberRepository.save(member);

    Member result = memberRepository.findBySocialTypeAndSocialId(
            member.getSocialType(), member.getSocialId())
        .orElseThrow(ExceptionNotFoundUser::new);

    assertEquals(member.getNickName(), result.getNickName());
    assertEquals(member.getEmail(), result.getEmail());
    assertEquals(member.getPassword(), result.getPassword());
    assertEquals(member.getAuthType(), result.getAuthType());
    assertEquals(member.getSocialType(), result.getSocialType());
  }
}