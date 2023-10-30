package com.project.lecture.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.ExceptionNotValidPassword;
import com.project.lecture.user.service.MemberService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

/**
 * 스프링 시큐리티의 폼 기반의 UsernamePasswordAuthenticationFilter를 참고하여 만든 커스텀 필터
 */
public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {

  private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
  private static final String HTTP_METHOD = "POST";
  private static final String CONTENT_TYPE = "application/json";
  private static final String USERNAME_KEY = "email";
  private static final String PASSWORD_KEY = "password";
  private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

  private final ObjectMapper objectMapper;
  private final MemberService memberService;
  private final PasswordEncoder encoder;

  public CustomLoginFilter(ObjectMapper objectMapper,  MemberService memberService,
      PasswordEncoder passwordEncoder) {
    super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
    this.memberService = memberService;
    this.encoder = passwordEncoder;
  }

  /**
   * 인증 처리 메소드 (Json 방식)
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException{
    if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
      throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
    }

    String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

    Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

    String email = usernamePasswordMap.get(USERNAME_KEY);
    String password = usernamePasswordMap.get(PASSWORD_KEY);


    Member member = memberService.getMemberByEmail(email);
    isValidPassword(password, member);

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

    return this.getAuthenticationManager().authenticate(authRequest);
  }

  private void isValidPassword(String password, Member member) {
    if (!encoder.matches(password, member.getPassword())) {
      throw new ExceptionNotValidPassword();
    }
  }
}

