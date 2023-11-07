package com.project.lecture.entity;

import com.project.lecture.type.StudyType;
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
import org.hibernate.annotations.ColumnDefault;

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
  private StudyType reminderType;

  @ColumnDefault("0")
  private boolean reminderComplete;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId")
  private Member member;

  public void changeCompleteIntoTrue() {
    this.reminderComplete = true;
  }
}
