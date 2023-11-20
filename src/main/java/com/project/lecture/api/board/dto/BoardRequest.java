package com.project.lecture.api.board.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String boardTitle;
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String boardContent;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Update {
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private Long boardId;
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String boardTitle;
    @NotBlank(message = "빈 값은 들어올 수 없습니다.")
    private String boardContent;
  }

}
