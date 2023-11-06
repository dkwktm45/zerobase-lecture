package com.project.lecture.api.reminder.application;

import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.reminder.dto.ReminderDto;
import com.project.lecture.api.reminder.dto.ReminderRequest.Create;
import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Course;
import com.project.lecture.entity.Lecture;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Reminder;
import com.project.lecture.entity.Study;
import com.project.lecture.exception.kind.ExceptionNotFoundReminder;
import com.project.lecture.type.StudyType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReminderApplication {

  private final ReminderService reminderService;
  private final MemberService memberService;

  private final PlannerService plannerService;
  private final StudyService studyService;
  private final CourseService courseService;
  private final LectureService lectureService;

  public void createReminderByRequestAndEmail(Create request, String email) {
    Member member = memberService.getMemberByEmail(email);

    reminderService.saveReminderByEntity(
        Reminder.builder()
            .member(member)
            .reminderType(request.getReminderType())
            .reminderTypeId(request.getReminderTypeId())
            .build()
    );
  }

  public void deleteReminderByIdAndEmail(Long id, String email) {
    if (!reminderService.existsByIdAndEmail(id, email)) {
      throw new ExceptionNotFoundReminder();
    }

    reminderService.deleteReminderById(id);

    if (plannerService.existByStudyIdAndType(id, StudyType.REMINDER)) {
      plannerService.deletePlanner(id, StudyType.REMINDER);
    }
  }




}
