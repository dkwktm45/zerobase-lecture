package com.project.lecture.repository;

import com.project.lecture.api.reminder.dto.ReminderDto;
import com.project.lecture.entity.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder,Long> {

  boolean existsByReminderIdAndMember_Email(Long id, String email);

}
