package com.project.lecture.api.board.controller;

import com.project.lecture.api.board.application.BoardApplication;
import com.project.lecture.api.board.dto.BoardRequest;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin/board")
public class AdminBoardController {

  private final BoardApplication boardApplication;
  @PostMapping("")
  public ResponseEntity<String> createBoardRequest(
      Principal principal,
      @RequestBody BoardRequest.Create request
  ){
    return ResponseEntity.ok(
        boardApplication.createBoard(principal.getName(), request)
    );
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateBoardRequest(
      Principal principal,
      @RequestBody BoardRequest.Update request
  ){
    return ResponseEntity.ok(
        boardApplication.updateBoard(principal.getName(), request)
    );
  }

  @GetMapping("")
  public ResponseEntity<String> deleteBoardRequest(
      Principal principal,
      @RequestParam(name = "boardId") Long boardId
  ){
    return ResponseEntity.ok(
        boardApplication.deleteBoard(principal.getName(), boardId)
    );
  }
}
