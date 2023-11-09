package com.project.lecture.type.adapter;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.TypeContent;
import com.project.lecture.entity.Course;
import com.project.lecture.exception.kind.ExceptionExistListening;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseAdapter implements TypeAdapter {
  private final ListenService listenService;
  private final CourseService courseService;
  @Override
  public boolean existCheck(Create create, String email, Long memberId) {
    return listenService.existCheck(create.getTypeId(), memberId);
  }

  @Override
  public TypeContent getContent(Long id) {
    Course course = courseService.getCourseById(id);
    return new TypeContent(course.getCourseName(),course.getCourseContent());
  }
  @Override
  public void exceptionThrow(){
    throw new ExceptionExistListening();
  }
}
