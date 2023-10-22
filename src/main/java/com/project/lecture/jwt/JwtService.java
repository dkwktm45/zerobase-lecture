package com.project.lecture.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String EMAIL_CLAIM = "email";
  private static final String BEARER = "Bearer ";

  private final MemberRepository memberRepository;
  private final RedisClient redisClient;

  /**
   * AccessToken 생성 메소드
   */
  public String createAccessToken(String email) {
    Date now = new Date();
    return JWT.create() // JWT 토큰을 생성하는 빌더 반환
        .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
        .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정

        //클레임으로는 저희는 email 하나만 사용합니다.
        //추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
        //추가하실 경우 .withClaim(클래임 이름, 클래임 값) 으로 설정해주시면 됩니다
        .withClaim(EMAIL_CLAIM, email)
        .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
  }

  /**
   * RefreshToken 생성
   * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
   */
  public String createRefreshToken() {
    Date now = new Date();
    return JWT.create()
        .withSubject(REFRESH_TOKEN_SUBJECT)
        .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
        .sign(Algorithm.HMAC512(secretKey));
  }

  /**
   * AccessToken 헤더에 실어서 보내기
   */
  public void sendAccessToken(HttpServletResponse response, String accessToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader(accessHeader, accessToken);
    log.info("재발급된 Access Token : {}", accessToken);
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
        .filter(refreshToken -> refreshToken.startsWith(BEARER))
        .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }

  /**
   * 헤더에서 AccessToken 추출
   */
  public Optional<String> getAccesstokenByReq(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessHeader))
        .filter(refreshToken -> refreshToken.startsWith(BEARER))
        .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }

  public Optional<String> getEmail(String accessToken) {
    try {
      return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
          .build()
          .verify(accessToken)
          .getClaim(EMAIL_CLAIM)
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
}


