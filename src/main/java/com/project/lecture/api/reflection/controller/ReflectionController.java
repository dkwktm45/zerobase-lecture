package com.project.lecture.api.reflection.controller;

import com.project.lecture.api.reflection.application.ReflectionApplication;
import com.project.lecture.api.reflection.dto.ReflectionRequest;
import com.project.lecture.type.ResponseType;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/reflection/")
@PreAuthorize("hasAuthority('USER')")
public class ReflectionController {

  private final ReflectionApplication reflectionApplication;

  @PostMapping("")
  public ResponseEntity<String> createReflectionRequest(
      @RequestBody
      @Valid
      ReflectionRequest.Create request,
      Principal principal
  ) {
    reflectionApplication.createReflectionByDtoAndEmail(request, principal.getName());

    return ResponseEntity.ok(
        ResponseType.INSERT_SUCCESS.getDescription()
    );
  }
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteReflectionRequest(
      @PathVariable("id") Long id,
      Principal principal
  ) {
    reflectionApplication.deleteReflectionByIdAndEmail(id, principal.getName());
    return ResponseEntity.ok(
        ResponseType.DELETE_SUCCESS.getDescription()
    );
  }
}
