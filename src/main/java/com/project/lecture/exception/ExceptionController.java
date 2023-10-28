package com.project.lecture.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
  @ExceptionHandler(SuperException.class)
  public ResponseEntity<ErrorResponse> handle404(final SuperException e){
    return ResponseEntity.badRequest().
        body(ErrorResponse.builder()
        .code(String.valueOf(e.getStatusCode()))
        .message(e.getMessage())
        .validation(e.getValidation())
        .build());
  }
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> notValidException(final MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    String detail = "";

    if (bindingResult.hasErrors()) {
      detail = bindingResult.getFieldError().getDefaultMessage();

      log.warn("잘못된 요청이 왔습니다. : {}",detail);

      return ResponseEntity.badRequest().body(
          ErrorResponse.builder()
              .code(HttpStatus.BAD_REQUEST.toString())
              .message(detail).build());
    }

    log.warn("잘못된 요청에 대해서 값을 못 찾고 있습니다.");
    return ResponseEntity.badRequest().body(
        ErrorResponse.builder()
            .code(HttpStatus.BAD_REQUEST.toString())
            .message(detail).build());
  }
}
