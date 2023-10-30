package com.project.lecture.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.lecture.Helper.CommonHelper;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.SuperException;
import com.project.lecture.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @InjectMocks
  private MemberService memberService;

  @Test
  @DisplayName("이메일 가져오기 - 성공")
  void getEmail() {
    String email = "planner@email.com";
    Member member = CommonHelper.createMemberForm();

    // given
    when(memberRepository.findByEmail(email))
        .thenReturn(Optional.of(member));

    // when
    Member result= memberService.getMemberByEmail(email);

    // then
    assertEquals(result.getEmail(), member.getEmail());
    assertEquals(result.getPassword(), member.getPassword());
    assertEquals(result.getAuthType(), member.getAuthType());
    assertEquals(result.getNickName(), member.getNickName());
    verify(memberRepository, timeout(1)).findByEmail(email);
  }
  @Test
  @DisplayName("이메일 가져오기 - 실패[empty]")
  void getEmail_fail() {
    String email = "planner@email.com";

    // given
    when(memberRepository.findByEmail(email))
        .thenReturn(Optional.empty());

    // when
    SuperException result = assertThrows(SuperException.class
        ,() -> memberService.getMemberByEmail(email));
    // then
    assertEquals(result.getMessage(), "이메일 존재하지 않습니다.");
    verify(memberRepository, timeout(1)).findByEmail(email);
  }
}