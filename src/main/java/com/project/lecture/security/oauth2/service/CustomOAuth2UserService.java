package com.project.lecture.security.oauth2.service;

import com.project.lecture.entity.Member;
import com.project.lecture.security.oauth2.CustomOAuth2User;
import com.project.lecture.security.oauth2.OAuthAttributes;
import com.project.lecture.repository.MemberRepository;
import com.project.lecture.type.SocialType;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;

  private static final String NAVER = "naver";
  private static final String KAKAO = "kakao";

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    SocialType socialType = getSocialType(registrationId);
    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails().getUserInfoEndpoint()
        .getUserNameAttributeName();
    Map<String, Object> attributes = oAuth2User.getAttributes();

    OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName,
        attributes);

    Member createdUser = getUser(extractAttributes, socialType);

    return new CustomOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(createdUser.getAuthType())),
        attributes,
        extractAttributes.getNameAttributeKey(),
        createdUser.getEmail(),
        createdUser.getAuthType()
    );
  }

  private SocialType getSocialType(String registrationId) {
    if (NAVER.equals(registrationId)) {
      return SocialType.NAVER;
    }
    if (KAKAO.equals(registrationId)) {
      return SocialType.KAKAO;
    }
    return SocialType.GOOGLE;
  }

  /**
   * SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드 만약 찾은 회원이 있다면, 그대로 반환하고 없다면
   */
  private Member getUser(OAuthAttributes attributes, SocialType socialType) {

    Member findUser = memberRepository.findBySocialTypeAndSocialId(socialType,
        attributes.getOauth2UserInfo().getId()).orElse(null);

    if (findUser == null) {
      return saveUser(attributes, socialType);
    }
    return findUser;
  }

  /**
   * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환 생성된 User 객체를 DB에 저장 : socialType,
   */
  private Member saveUser(OAuthAttributes attributes, SocialType socialType) {
    Member createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
    return memberRepository.save(createdUser);
  }
}

