package com.project.lecture.api.board.service;

import com.project.lecture.entity.Board;
import com.project.lecture.exception.kind.ExceptionNotFoundBoard;
import com.project.lecture.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

  private final BoardRepository boardRepository;

  public void saveEntity(Board board) {
    boardRepository.save(board);
  }

  public Board getBoardById(Long boardId) {
    return boardRepository.findById(boardId)
        .orElseThrow(ExceptionNotFoundBoard::new);
  }

  public void deleteEntity(Board board) {
    boardRepository.delete(board);
  }

  public Page<Board> getBoardsByPage(Pageable pageable) {
    return boardRepository.findAll(pageable);
  }
}
