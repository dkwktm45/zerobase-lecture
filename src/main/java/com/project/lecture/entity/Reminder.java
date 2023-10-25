package com.project.lecture.entity;

import com.project.lecture.type.converter.StudyConverter;
import javax.persistence.Convert;
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

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reminderId;
  private Long reminderTypeId;
  @Convert(converter = StudyConverter.class)
  private StudyConverter reminderType;
  private boolean reminderComplete;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId")
  private Member member;
}
