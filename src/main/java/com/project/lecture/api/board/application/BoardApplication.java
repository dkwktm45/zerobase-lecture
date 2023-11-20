package com.project.lecture.api.board.application;

import com.project.lecture.api.board.dto.BoardDto;
import com.project.lecture.api.board.dto.BoardRequest;
import com.project.lecture.api.board.dto.BoardsDto;
import com.project.lecture.api.board.dto.BoardRequest.Create;
import com.project.lecture.api.board.service.BoardService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Board;
import com.project.lecture.entity.Member;
import com.project.lecture.exception.kind.ExceptionNotValidUser;
import com.project.lecture.type.ResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardApplication {

  private final BoardService boardService;
  private final MemberService memberService;

  public String createBoard(String email, Create request) {
    Member member = memberService.getMemberByEmail(email);
    boardService.saveEntity(Board.toEntity(request, member));
    return ResponseType.INSERT_SUCCESS.getDescription();
  }

  @Transactional
  public String updateBoard(String email, BoardRequest.Update request) {
    Board board = boardService.getBoardById(request.getBoardId());

    if (!board.getMember().getEmail().equals(email)) {
      throw new ExceptionNotValidUser();
    }

    board.updateBoard(request);
    return ResponseType.CHANGE_SUCCESS.getDescription();
  }

  public String deleteBoard(String email, Long boardId) {
    Board board = boardService.getBoardById(boardId);

    if (!board.getMember().getEmail().equals(email)) {
      throw new ExceptionNotValidUser();
    }

    boardService.deleteEntity(board);
    return ResponseType.DELETE_SUCCESS.getDescription();
  }

  public Page<BoardsDto> getBoards(Pageable pageable) {
    Page<Board> boardPage = boardService.getBoardsByPage(pageable);
    return BoardsDto.toDtoList(boardPage);
  }

  public BoardDto getBoard(Long boardId) {
    return BoardDto.fromEntity(boardService.getBoardById(boardId));
  }
}
