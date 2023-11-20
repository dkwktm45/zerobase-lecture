package com.project.lecture.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import com.project.lecture.api.board.dto.BoardRequest;
import com.project.lecture.api.board.dto.BoardRequest.Update;
import com.project.lecture.entity.date.BaseEntity;
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
public class Board extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long boardId;

  private String boardTitle;
  private String boardContent;
  private LocalDate updateDt;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "memberId")
  private Member member;

  public static Board toEntity(BoardRequest.Create request, Member member) {
    return Board.builder()
        .boardContent(request.getBoardContent())
        .boardTitle(request.getBoardTitle())
        .member(member)
        .updateDt(LocalDate.now())
        .build();
  }

  public void updateBoard(Update request) {
    this.boardContent = request.getBoardContent();
    this.boardTitle = request.getBoardTitle();
    this.updateDt = LocalDate.now();
  }
}
