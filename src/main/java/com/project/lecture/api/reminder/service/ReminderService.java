package com.project.lecture.api.reminder.service;

import com.project.lecture.entity.Reminder;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import com.project.lecture.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderService {

  private final ReminderRepository reminderRepository;

  public void saveReminderByEntity(Reminder reminder) {
    reminderRepository.save(reminder);
  }

  public boolean existsByIdAndEmail(Long id, String email) {
    return reminderRepository
        .existsByReminderIdAndMember_Email(id,email);
  }

  public void deleteReminderById(Long id) {
    reminderRepository.deleteById(id);
  }

  public Reminder getReminderById(Long id) {
    return reminderRepository.findById(id)
        .orElseThrow(ExceptionNotFoundReminder::new);
  }

  public Page<Reminder> getListByEmailAndPage(String email, Pageable pageable) {
    return reminderRepository.findByMember_Email(email,pageable);
  }

}
