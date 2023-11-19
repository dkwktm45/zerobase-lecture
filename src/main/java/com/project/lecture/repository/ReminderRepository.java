package com.project.lecture.repository;

import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReminderRepository extends JpaRepository<Reminder,Long> {

  boolean existsByReminderIdAndMember_Email(Long id, String email);

  @Query(value = "select * from reminder r "
      + "where reminderId = :id and "
      + "r.memberId = :memberId limit 1", nativeQuery = true)
  Reminder getIfExistsByReminderIdAndMemberId(Long id, Long memberId);

  Page<Reminder> findByMember_Email(String email, Pageable pageable);

  List<Reminder> findAllByMemberAndReminderComplete(Member member, boolean reminderComplete);
}
