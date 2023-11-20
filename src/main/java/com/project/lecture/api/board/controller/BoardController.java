package com.project.lecture.api.board.controller;


import com.project.lecture.api.board.application.BoardApplication;
import com.project.lecture.api.board.dto.BoardDto;
import com.project.lecture.api.board.dto.BoardsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequestMapping("/board")
public class BoardController {

  private final BoardApplication boardApplication;
  @GetMapping("/list")
  public ResponseEntity<Page<BoardsDto>> getBoardsByRequest(
      Pageable pageable
  ) {
    return ResponseEntity.ok(
        boardApplication.getBoards(pageable)
    );
  }

  @GetMapping("/{boardId}")
  public ResponseEntity<BoardDto> getBoardByRequest(
      @PathVariable(name = "boardId")
      Long boardId
  ) {
    return ResponseEntity.ok(
        boardApplication.getBoard(boardId)
    );
  }
}
