package com.project.lecture.security.oauth2;

import com.project.lecture.entity.Member;
import com.project.lecture.jwt.PasswordUtil;
import com.project.lecture.security.oauth2.social.kind.GoogleOAuth2UserInfo;
import com.project.lecture.security.oauth2.social.kind.KakaoOAuth2UserInfo;
import com.project.lecture.security.oauth2.social.kind.NaverOAuth2UserInfo;
import com.project.lecture.security.oauth2.social.OAuth2UserInfo;
import com.project.lecture.type.AuthType;
import com.project.lecture.type.SocialType;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
 */
@Getter
public class OAuthAttributes {

  private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
  private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

  @Builder
  public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
    this.nameAttributeKey = nameAttributeKey;
    this.oauth2UserInfo = oauth2UserInfo;
  }

  public static OAuthAttributes of(SocialType socialType,
      String userNameAttributeName, Map<String, Object> attributes) {

    if (socialType == SocialType.NAVER) {
      return ofNaver(userNameAttributeName, attributes);
    }
    if (socialType == SocialType.KAKAO) {
      return ofKakao(userNameAttributeName, attributes);
    }
    return ofGoogle(userNameAttributeName, attributes);
  }

  private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .nameAttributeKey(userNameAttributeName)
        .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
        .build();
  }

  public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .nameAttributeKey(userNameAttributeName)
        .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
        .build();
  }

  public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .nameAttributeKey(userNameAttributeName)
        .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
        .build();
  }

  public Member toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
    return Member.builder()
        .socialType(socialType)
        .socialId(oauth2UserInfo.getId())
        .password(PasswordUtil.generateRandomPassword())
        .email(oauth2UserInfo.getEmail())
        .nickName(oauth2UserInfo.getNickname())
        .authType(AuthType.GUEST.getDescription())
        .build();
  }
}

