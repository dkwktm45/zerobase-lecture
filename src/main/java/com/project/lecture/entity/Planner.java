package com.project.lecture.entity;

import com.project.lecture.type.TypeRequest;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.converter.StudyConverter;
import java.time.LocalDate;
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
public class Planner {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long plannerId;
  private Long plannerTypeId;

  @Convert(converter = StudyConverter.class)
  private StudyType plannerType;
  private LocalDate plannerDt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId")
  private Member member;
  @ColumnDefault(value = "0")
  private boolean plannerComplete;

  public static Planner toEntity(TypeRequest.Create dto, Member member) {
    return Planner.builder()
        .plannerType(dto.getType())
        .plannerTypeId(dto.getTypeId())
        .member(member)
        .plannerDt(dto.getPlannerDt())
        .build();
  }

  public void updateDate(LocalDate plannerDt) {
    this.plannerDt = plannerDt;
  }

  public void changeComplete() {
    this.plannerComplete = true;
  }
}
