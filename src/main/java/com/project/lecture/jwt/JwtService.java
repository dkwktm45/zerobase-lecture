package com.project.lecture.jwt;

import static com.project.lecture.jwt.descripton.JwtDescription.ACCESS_TOKEN_SUBJECT;
import static com.project.lecture.jwt.descripton.JwtDescription.EMAIL_CLAIM;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.lecture.jwt.descripton.JwtDescription;
import com.project.lecture.redis.RedisClient;
import com.project.lecture.redis.dto.RefreshToken;
import com.project.lecture.repository.MemberRepository;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.access.expiration}")
  private Long accessTokenExpirationPeriod;

  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpirationPeriod;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Value("${jwt.refresh.header}")
  private String refreshHeader;


  private final MemberRepository memberRepository;
  private final RedisClient redisClient;

  /**
   * AccessToken 생성 메소드
   */
  public String createAccessToken(String email) {
    Date now = new Date();
    return JWT.create() // JWT 토큰을 생성하는 빌더 반환
        .withSubject(ACCESS_TOKEN_SUBJECT.getValue()) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
        .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정

        .withClaim(EMAIL_CLAIM.getValue(), email)
        .sign(Algorithm.HMAC512(secretKey));
  }

  /**
   * RefreshToken 생성
   */
  public String createRefreshToken() {
    Date now = new Date();
    return JWT.create()
        .withSubject(JwtDescription.REFRESH_TOKEN_SUBJECT.getValue())
        .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
        .sign(Algorithm.HMAC512(secretKey));
  }
  public RefreshToken getTokenInfoByRedis(String accessToken) {
    return redisClient.get(accessToken, RefreshToken.class);
  }
  /**
   * AccessToken + RefreshToken 헤더에 실어서 보내기
   */
  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);

    setAccessTokenHeader(response, accessToken);
    setRefreshTokenHeader(response, refreshToken);
    log.info("Access Token, Refresh Token 헤더 설정 완료");
  }

  /**
   * 헤더에서 RefreshToken 추출
   */
  public Optional<String> getRefreshtokenByReq(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshHeader))
        .filter(refreshToken -> refreshToken.startsWith(JwtDescription.BEARER.getValue()))
        .map(refreshToken -> refreshToken.replace(JwtDescription.BEARER.getValue(), ""));
  }

  /**
   * 헤더에서 AccessToken 추출
   */
  public Optional<String> getAccesstokenByReq(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessHeader))
        .filter(refreshToken -> refreshToken.startsWith(JwtDescription.BEARER.getValue()))
        .map(refreshToken -> refreshToken.replace(JwtDescription.BEARER.getValue(), ""));
  }

  public Optional<String> getEmail(String accessToken) {
    try {
      return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
          .build()
          .verify(accessToken)
          .getClaim(EMAIL_CLAIM.getValue())
          .asString());
    } catch (Exception e) {
      log.error("액세스 토큰이 유효하지 않습니다.");
      return Optional.empty();
    }
  }


  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(accessHeader, accessToken);
  }


  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(refreshHeader, refreshToken);
  }

  public void updateRefreshToken(String accessToken, String email, String refreshToken) {
    log.info("토큰 업데이트");
    redisClient.put(accessToken ,
        new RefreshToken(email,accessToken,refreshToken)
    );
  }

  public boolean isTokenValid(String token) {
    try {
      JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
      return true;
    } catch (Exception e) {
      log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
      return false;
    }
  }

  public void saveToken(String newAccessToken, RefreshToken tokenInfo) {
    redisClient.put(newAccessToken,tokenInfo);
  }
}


