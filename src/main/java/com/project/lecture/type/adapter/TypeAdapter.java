package com.project.lecture.type.adapter;

import com.project.lecture.entity.Member;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.TypeContent;

public interface TypeAdapter {
  boolean existCheck(Create create, String email, Long id);

  TypeContent getContent(Long id);
  void exceptionThrow();
  void complete(Long id, Member member);
  StudyType getStudyType();
}
