package com.project.lecture.entity;

import com.project.lecture.type.SocialType;
import com.project.lecture.type.converter.SocialConverter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

  @Id
  @Column(name = "memberId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Column(nullable = false, unique = true)
  private String email;
  private String nickName;
  private String password;

  private String authType;
  @Convert(converter = SocialConverter.class)
  private SocialType socialType;
  private String socialId;

  @OneToMany(mappedBy = "member")
  @Builder.Default
  private List<Course> courses = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  @Builder.Default
  private List<Reminder> reminders = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Listening> listenings = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  @Builder.Default
  private List<Study> studies = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  @Builder.Default
  private List<Board> boards = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  @Builder.Default
  private List<Planner> planners = new ArrayList<>();

  @Transactional
  public void setPassword(String password) {
    this.password = password;
  }

  public Member updateBySocialLogin(String nickName, SocialType socialType) {
    this.nickName = nickName;
    this.socialType = socialType;
    return this;
  }

  public void authToUser() {
    this.authType = "USER";
  }
}
