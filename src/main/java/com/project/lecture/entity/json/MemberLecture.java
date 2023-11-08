package com.project.lecture.entity.json;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberLecture {
  private Long id;
  private int time;
  private LocalDate localDate;
}
