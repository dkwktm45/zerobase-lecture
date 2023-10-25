package com.project.lecture.security.oauth2;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

/**
 * DefaultOAuth2User를 상속하고, email과 role 필드를 추가로 가진다.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

  private String email;
  private String authType;

  public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes, String nameAttributeKey,
      String email, String authType) {
    super(authorities, attributes, nameAttributeKey);
    this.email = email;
    this.authType = authType;
  }
}