package com.project.lecture.api.Listen.controller;

import static com.project.lecture.type.ResponseType.DELETE_SUCCESS;
import static com.project.lecture.type.ResponseType.INSERT_SUCCESS;

import com.project.lecture.api.Listen.application.ListenApplication;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/listen/")
@PreAuthorize("hasAuthority('USER')")
public class ListenController {

  private final ListenApplication listenApplication;

  @PutMapping("/course/{courseId}")
  public ResponseEntity<?> userListenRequest(
      @PathVariable("courseId") Long courseId,
      Principal principal
  ) {
    listenApplication.listenCourse(courseId, principal.getName());

    return ResponseEntity.ok(
        INSERT_SUCCESS.getDescription()
    );
  }

  @DeleteMapping("/{listenId}")
  public ResponseEntity<?> deleteListenRequest(
      @PathVariable("listenId") Long courseId,
      Principal principal
  ) {
    listenApplication.deleteListenCourse(courseId, principal.getName());

    return ResponseEntity.ok(
        DELETE_SUCCESS.getDescription()
    );
  }
}
