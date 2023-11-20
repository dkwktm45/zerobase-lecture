package com.project.lecture.api.board.dto;

import com.project.lecture.entity.Board;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {


  private Long boardId;
  private String boardWriter;
  private String boardTitle;
  private String boardContent;
  private LocalDate updateDt;

  public static BoardDto fromEntity(Board board) {
    return BoardDto.builder()
        .boardId(board.getBoardId())
        .boardTitle(board.getBoardTitle())
        .boardContent(board.getBoardContent())
        .boardWriter(board.getMember().getNickName())
        .build();
  }
}
