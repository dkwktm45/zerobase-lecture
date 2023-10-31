package com.project.lecture.entity.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @Column(nullable = false,updatable = false)
  @CreatedDate
  private String createDt;

  @PrePersist
  public void onPrePersist() {
    this.createDt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }
}
