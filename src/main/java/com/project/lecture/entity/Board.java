package com.project.lecture.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long boardId;

  private String boardTitle;
  private String boardContent;
  private LocalDate createdAt;
  private LocalDate updateAt;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "memberId")
  private Member member;
}
