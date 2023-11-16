package com.project.lecture.api.review.controller;

import com.project.lecture.api.review.application.ReviewApplication;
import com.project.lecture.api.review.dto.ReviewDto;
import com.project.lecture.api.review.dto.ReviewRequest;
import com.project.lecture.type.ResponseType;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/review")
public class ReviewController {

  private final ReviewApplication reviewApplication;

  @PostMapping("")
  public ResponseEntity<String> createReviewRequest(
      @RequestBody @Valid ReviewRequest.Create request,
      Principal principal
  ) {
    reviewApplication.createByDtoAndEmail(request, principal.getName());
    return ResponseEntity.ok(ResponseType.INSERT_SUCCESS.getDescription());
  }
  @GetMapping("/list")
  public ResponseEntity<Page<ReviewDto>> getListRequest(
      @RequestParam("courseId") Long courseId,
      @PageableDefault(page = 1) Pageable pageable
  ) {
    return ResponseEntity.ok(
        reviewApplication.getReviewListById(courseId,pageable)
    );
  }
}
