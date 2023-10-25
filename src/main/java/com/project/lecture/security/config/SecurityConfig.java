package com.project.lecture.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lecture.jwt.JwtAuthenticationProcessingFilter;
import com.project.lecture.jwt.JwtService;
import com.project.lecture.oauth2.handler.OAuth2LoginFailureHandler;
import com.project.lecture.oauth2.handler.OAuth2LoginSuccessHandler;
import com.project.lecture.oauth2.service.CustomOAuth2UserService;
import com.project.lecture.redis.RedisClient;
import com.project.lecture.repository.MemberRepository;
import com.project.lecture.security.CustomLoginFilter;
import com.project.lecture.security.handler.LoginFailHandler;
import com.project.lecture.security.handler.LoginSuccessHandler;
import com.project.lecture.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
  private final MemberRepository memberRepository;
  private final ObjectMapper objectMapper;
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final RedisClient redisClient;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin().disable() // FormLogin 사용 X
        .httpBasic().disable() // httpBasic 사용 X
        .csrf().disable() // csrf 보안 사용 X
        .headers().frameOptions().disable()
        .and()

        // 세션 사용하지 않으므로 STATELESS로 설정
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()

        //== URL별 권한 관리 옵션 ==//
        .authorizeRequests()

        // 나중에 사용 가능성 보류
//        .antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
        .antMatchers("/join", "/login").permitAll()
        .anyRequest().authenticated()
        .and()
        //== 소셜 로그인 설정 ==//
        .oauth2Login()
        .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
        .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
        .userInfoEndpoint().userService(customOAuth2UserService); // customUserService 설정

    // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
    // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
    // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
    http.addFilterAfter(customLoginFilter(), LogoutFilter.class);
    http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomLoginFilter.class);

    return http.build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
    return new LoginSuccessHandler(jwtService, redisClient);
  }

  /**
   * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
   */
  @Bean
  public LoginFailHandler loginFailureHandler() {
    return new LoginFailHandler();
  }

  /**
   * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
   * setAuthenticationManager(authenticationManager())로 위에서 등록한
   * AuthenticationManager(ProviderManager) 설정 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한
   * handler 설정
   */
  @Bean
  public CustomLoginFilter customLoginFilter() {
    CustomLoginFilter customJsonUsernamePasswordLoginFilter
        = new CustomLoginFilter(objectMapper, memberRepository, passwordEncoder());
    customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
    customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
    return customJsonUsernamePasswordLoginFilter;
  }

  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(jwtService, memberRepository, redisClient);
  }
}