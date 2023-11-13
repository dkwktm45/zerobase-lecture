package com.project.lecture.api.complete.dto;

import java.time.LocalDate;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddMemberLecture {

  private int time;
  private HashMap<Long , LocalDate> memberLectures;
}
