package com.project.lecture.api.planner.application;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.planner.dto.PlannerRequest.Update;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.exception.kind.ExceptionNotFoundPlanner;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Planner;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.adapter.CourseAdapter;
import com.project.lecture.type.adapter.LectureAdapter;
import com.project.lecture.type.adapter.ReminderAdapter;
import com.project.lecture.type.adapter.StudyAdapter;
import com.project.lecture.type.adapter.TypeAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerApplication {

  private final PlannerService plannerService;
  private final MemberService memberService;

  private final StudyService studyService;
  private final LectureService lectureService;
  private final CourseService courseService;
  private final ListenService listenService;
  private final ReminderService reminderService;
  private Map<StudyType, TypeAdapter> adapters;

  @PostConstruct
  public void setUp() {
    adapters = new HashMap<>();
    adapters.put(StudyType.STUDY, new StudyAdapter(studyService));
    adapters.put(StudyType.COURSE, new CourseAdapter(listenService, courseService));
    adapters.put(StudyType.REMINDER, new ReminderAdapter(reminderService));
    adapters.put(StudyType.LECTURE, new LectureAdapter(listenService, lectureService));
  }
  public void createPlannerByRequestAndEmail(Create request, String email) {
    if (request.getPlannerDt() == null) {
      throw new NullPointerException("날짜가 존재하지 않습니다.");
    }

    Member member = memberService.getMemberByEmail(email);

    TypeAdapter adapter = adapters.get(request.getType());

    if (adapter == null){
      throw new UnsupportedOperationException("타당하지 않는 타입입니다.");
    }
    if (!adapter.existCheck(request, email, member.getMemberId())) {
      adapter.exceptionThrow();
    }

    plannerService.saveEntity(Planner.toEntity(request, member));
  }

  public void updatePlannerByRequestAndEmail(Update request, String email) {
    if (!plannerService.existPlannerByIdAndEmail(request.getPlannerId(), email)) {
      throw new ExceptionNotFoundPlanner();
    }

    plannerService.changeDate(request);
  }
}
