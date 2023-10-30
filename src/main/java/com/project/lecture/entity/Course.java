package com.project.lecture.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long courseId;
  private String courseName;
  @Lob
  private String courseContent;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  @Builder.Default
  private List<Lecture> lectures = new ArrayList<>();

  @OneToMany(mappedBy = "course")
  private List<Listening> listenings = new ArrayList<>();

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  @Builder.Default
  private List<Review> reviews = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId")
  private Member member;
  public void setMember(Member member) { this.member = member; }
  public void changeValues(Course change) {
    this.courseContent = change.getCourseContent();
    this.courseName = change.getCourseName();
  }
}
