package com.project.lecture.type.adapter;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.TypeContent;
import com.project.lecture.entity.Lecture;
import com.project.lecture.exception.kind.ExceptionExistListening;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LectureAdapter implements TypeAdapter {
  private final ListenService listenService;
  private final LectureService lectureService;
  @Override
  public boolean existCheck(Create create, String email, Long memberId) {
    Lecture lecture = lectureService.getLectureById(create.getTypeId());
    return listenService.existCheck(lecture.getCourse().getCourseId(), memberId);
  }

  @Override
  public TypeContent getContent(Long id) {
    Lecture lecture = lectureService.getLectureById(id);
    return new TypeContent(lecture.getLectureName(),null);
  }
  @Override
  public void exceptionThrow(){
    throw new ExceptionExistListening();
  }
}
