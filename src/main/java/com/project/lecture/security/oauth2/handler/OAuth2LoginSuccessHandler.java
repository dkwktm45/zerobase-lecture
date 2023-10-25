package com.project.lecture.security.oauth2.handler;


import static com.project.lecture.jwt.descripton.JwtDescription.ACCESS_TOKEN_SUBJECT;
import static com.project.lecture.jwt.descripton.JwtDescription.REFRESH_TOKEN_SUBJECT;
import static com.project.lecture.security.oauth2.cookie.HttpCookieOauth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.EmptyTargetUrl;
import com.project.lecture.jwt.JwtService;
import com.project.lecture.security.oauth2.CustomOAuth2User;
import com.project.lecture.security.oauth2.cookie.HttpCookieOauth2AuthorizationRequestRepository;
import com.project.lecture.security.oauth2.cookie.CookieUtils;
import com.project.lecture.type.AuthType;
import com.project.lecture.user.service.MemberService;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final MemberService memberService;
  private final HttpCookieOauth2AuthorizationRequestRepository cookieOauth2AuthorizationRequestRepository;

  /**
   * 토큰의 만료시간을 지정했기 때문에 해당 로그인을 다시 한번 더 한 경우 및 최소 로그인시 토큰을 다시 생성한다.
   * */
  @Override
  @Transactional
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("OAuth2 Login 성공!");
    try {
      CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
      Member member = memberService.getEmail(oAuth2User.getEmail());

      if (member.getAuthType().equals(AuthType.GUEST.getDescription())) {
        member.authToUser();
      }

      String result = setUrlAndToken(
          getTargetUrl(request), response, oAuth2User
      );
      clearAuthenticationAttributes(request, response);

      String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
      String refreshToken = jwtService.createRefreshToken();
      jwtService.updateRefreshToken(accessToken, oAuth2User.getEmail(), refreshToken);
      response.setHeader(ACCESS_TOKEN_SUBJECT.getValue(), accessToken);
      response.setHeader(REFRESH_TOKEN_SUBJECT.getValue(), refreshToken);

      getRedirectStrategy().sendRedirect(request, response, result);
    } catch (Exception e) {
      throw e;
    }

  }

  private String setUrlAndToken(String targetUrl, HttpServletResponse response, CustomOAuth2User oAuth2User) {

    return UriComponentsBuilder.fromUriString(targetUrl)
        .build().toUriString();
  }

  private String getTargetUrl(HttpServletRequest request) {
    String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue).orElseGet(null);

    if (Objects.isNull(targetUrl)) {
      throw new EmptyTargetUrl(REDIRECT_URI_PARAM_COOKIE_NAME);
    }

    return targetUrl;
  }


  protected void clearAuthenticationAttributes(HttpServletRequest request,
      HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    cookieOauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
  }
}
