package com.project.lecture.api.board.dto;

import com.project.lecture.entity.Board;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardsDto {

  private Long boardId;
  private String boardTitle;
  private LocalDate updateDt;

  public static BoardsDto fromEntity(Board board) {
    return BoardsDto.builder()
        .boardId(board.getBoardId())
        .boardTitle(board.getBoardTitle())
        .updateDt(board.getUpdateDt())
        .build();
  }

  public static Page<BoardsDto> toDtoList(Page<Board> boards){
    return boards.map(BoardsDto::fromEntity);
  }
}
