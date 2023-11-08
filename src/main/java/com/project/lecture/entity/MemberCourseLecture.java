package com.project.lecture.entity;

import com.project.lecture.entity.json.MemberLecture;
import com.vladmihalcea.hibernate.type.json.JsonType;
import java.util.HashMap;
import javax.persistence.Column;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "json", typeClass = JsonType.class)
public class MemberCourseLecture {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Type(type = "json")
  @Column(columnDefinition = "longtext")
  private HashMap<Long, MemberLecture> memberLectures = new HashMap<>();

  private Long courseId;

  public void updateMemberLecture(HashMap<Long, MemberLecture> memberLectures) {
    this.memberLectures = memberLectures;
  }
}
