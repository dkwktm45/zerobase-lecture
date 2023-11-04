package com.project.lecture.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reflection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reflectionId;

  private String reflectionTitle;
  private String reflectionContent;
  @ColumnDefault("0")
  private boolean reflectionComplete;
  private String weekDt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId")
  private Member member;

  public void changeCompleteIntoTrue() {
    this.reflectionComplete = true;
  }
}
