package com.project.lecture.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long lectureId;

  private String lectureName;
  private int lectureTime;

  @ColumnDefault("0")
  private boolean lectureComplete;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "courseId")
  private Course course;
}
