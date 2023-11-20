package com.project.lecture.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lecture.jwt.JwtAuthenticationProcessingFilter;
import com.project.lecture.jwt.JwtService;
import com.project.lecture.redis.TokenClient;
import com.project.lecture.security.CustomLoginFilter;
import com.project.lecture.security.handler.LoginFailHandler;
import com.project.lecture.security.handler.LoginSuccessHandler;
import com.project.lecture.security.oauth2.cookie.HttpCookieOauth2AuthorizationRequestRepository;
import com.project.lecture.security.oauth2.handler.OAuth2LoginFailureHandler;
import com.project.lecture.security.oauth2.handler.OAuth2LoginSuccessHandler;
import com.project.lecture.security.oauth2.service.CustomOAuth2UserService;
import com.project.lecture.security.service.CustomUserDetailsService;
import com.project.lecture.api.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final JwtService jwtService;
  private final MemberService memberService;
  private final ObjectMapper objectMapper;
  private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final TokenClient tokenClient;

  @Bean
  public HttpCookieOauth2AuthorizationRequestRepository cookieOauth2AuthorizationRequestRepository() {
    return new HttpCookieOauth2AuthorizationRequestRepository();
  }
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin().disable()
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()

        .authorizeRequests()
        .antMatchers("/v2/api-docs",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/join","/login","/oauth2","/swagger-ui/**").permitAll()
        .antMatchers("/admin/**").hasAnyRole("ADMIN")
        .anyRequest().authenticated()
        .and()
        .oauth2Login()
          .authorizationEndpoint().baseUri("/oauth2/authorization")
          .authorizationRequestRepository(cookieOauth2AuthorizationRequestRepository())
        .and()
          .redirectionEndpoint().baseUri("/*/oauth2/code/*")
        .and()
          .userInfoEndpoint().userService(customOAuth2UserService)
        .and()
          .successHandler(oAuth2LoginSuccessHandler())
          .failureHandler(oAuth2LoginFailureHandler);
    http.addFilterAfter(customLoginFilter(), LogoutFilter.class);
    http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomLoginFilter.class);

    http.cors();

    return http.build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
    return new OAuth2LoginSuccessHandler(jwtService,
        cookieOauth2AuthorizationRequestRepository());
  }

  /**
   * AuthenticationManager 설정 후 등록 PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는
   * 위에서 등록한 PasswordEncoder 사용) FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
   * UserDetailsService는 커스텀 LoginService로 등록 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인
   * ProviderManager 사용(return ProviderManager)
   */
  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(customUserDetailsService);
    return new ProviderManager(provider);
  }

  /**
   * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
   */
  @Bean
  public LoginSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler(jwtService, tokenClient);
  }

  /**
   * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
   */
  @Bean
  public LoginFailHandler loginFailureHandler() {
    return new LoginFailHandler();
  }

  /**
   * CustomJsonUsernamePasswordAuthenticationFilter
   */
  @Bean
  public CustomLoginFilter customLoginFilter() {
    CustomLoginFilter customJsonUsernamePasswordLoginFilter
        = new CustomLoginFilter(objectMapper, memberService, passwordEncoder());
    customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
    customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
    return customJsonUsernamePasswordLoginFilter;
  }

  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(jwtService,memberService);
  }
}