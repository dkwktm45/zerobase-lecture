package com.project.lecture.jwt;

import static com.project.lecture.type.UrlCheck.NO_CHECK_JOIN;
import static com.project.lecture.type.UrlCheck.NO_CHECK_LOGIN;
import static com.project.lecture.type.UrlCheck.NO_CHECK_OAUTH2;

import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.ExceptionNotValidToken;
import com.project.lecture.jwt.descripton.JwtDescription;
import com.project.lecture.redis.dto.RefreshToken;
import com.project.lecture.user.service.MemberService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt 인증 필터 "/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 * <p>
 * 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청 AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 * <p>
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다. 2. RefreshToken이
 * 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR 3. RefreshToken이 있는 경우 -> DB의
 * RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식) 인증 성공 처리는 하지 않고 실패 처리
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final MemberService memberService;


  private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String url = request.getRequestURI();
    if (url.startsWith(NO_CHECK_JOIN.getUrl()) ||
        url.startsWith(NO_CHECK_LOGIN.getUrl()) ||
        url.startsWith(NO_CHECK_OAUTH2.getUrl())) {
      filterChain.doFilter(request, response);
      return;
    }

    // refresh token 요청일 경우 access, refresh를 같이 보냄
    // 사용자 요청 헤더에서 RefreshToken 추출
    // -> RefreshToken이 없거나 유효하지 않다면 null을 반환
    // 사용자의 요청 헤더에 RefreshToken이 있는 경우는,
    String refreshToken = jwtService.getRefreshtokenByReq(request)
        .filter(jwtService::isTokenValid)
        .orElse(null);

    if (refreshToken != null) {
      jwtService.getAccesstokenByReq(request)
          .ifPresent(
              s -> checkRefreshTokenAndReIssueAccessToken(response, refreshToken, s)
          );
      return;
    }

    checkAccessTokenAndAuthentication(request, response, filterChain);
  }

  /**
   * [accessToken 토큰으로 유저 정보 찾기 & 액세스 토큰 / 리프레시 토큰 재발급 메소드]
   */
  public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
      String refreshToken,
      String accessToken) {
    log.info("토큰 재발급");
    String getRefreshToken = jwtService.getTokenInfoByRedis(accessToken);

    if (!getRefreshToken.equals(refreshToken)) {
      throw new ExceptionNotValidToken(JwtDescription.ACCESS_TOKEN_SUBJECT.getValue());
    }

    String email = jwtService.getEmail(accessToken)
        .orElseGet(null);

    if (email == null) {
      throw new ExceptionNotValidToken(email);
    }

    String newRefreshToken = jwtService.createRefreshToken();
    String newAccessToken = jwtService.createAccessToken(email);

    jwtService.saveToken(newAccessToken, newRefreshToken);
    jwtService.sendAccessAndRefreshToken(response, newAccessToken, newRefreshToken);
  }


  /**
   * [액세스 토큰 체크 & 인증 유효기간 체크]
   */
  public void checkAccessTokenAndAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("인증 유효성 검사");
    jwtService.getAccesstokenByReq(request)
        .filter(jwtService::isTokenValid)
        .ifPresent(accessToken -> jwtService.getEmail(accessToken)
            .ifPresent(email -> Optional.of(memberService.getEmail(email))
                .ifPresent(this::saveAuthentication)));

    filterChain.doFilter(request, response);
  }

  public void saveAuthentication(Member myUser) {
    log.info("security context 올리기");
    String password = myUser.getPassword();
    if (password == null) {
      password = PasswordUtil.generateRandomPassword();
      myUser.setPassword(password);
    }

    UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
        .username(myUser.getEmail())
        .password(password)
        .roles(myUser.getAuthType())
        .build();

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(userDetailsUser, null,
            authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

}