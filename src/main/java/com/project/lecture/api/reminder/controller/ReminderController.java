package com.project.lecture.api.reminder.controller;

import com.project.lecture.api.reflection.dto.ReflectionDto;
import com.project.lecture.api.reminder.application.ReminderApplication;
import com.project.lecture.api.reminder.dto.ReminderDto;
import com.project.lecture.api.reminder.dto.ReminderRequest;
import com.project.lecture.type.ResponseType;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/reminder/")
@PreAuthorize("hasAuthority('USER')")
public class ReminderController {

  private final ReminderApplication reminderApplication;

  @PostMapping("")
  public ResponseEntity<String> createReminderRequest(
      @RequestBody @Valid ReminderRequest.Create request,
      Principal principal
  ) {
    reminderApplication.createReminderByRequestAndEmail(request, principal.getName());
    return ResponseEntity.ok(
        ResponseType.INSERT_SUCCESS.getDescription()
    );
  }

  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteReflectionRequest(
      @PathVariable("id") Long id,
      Principal principal
  ) {
    reminderApplication.deleteReminderByIdAndEmail(id, principal.getName());
    return ResponseEntity.ok(
        ResponseType.DELETE_SUCCESS.getDescription()
    );
  }

  @PutMapping("/complete")
  public ResponseEntity<String> completeReflectionRequest(
      @RequestParam("id") Long id,
      Principal principal
  ) {
    reminderApplication.completeByIdAndEmail(id, principal.getName());
    return ResponseEntity.ok(
        ResponseType.COMPLETE_SUCCESS.getDescription()
    );
  }

}
