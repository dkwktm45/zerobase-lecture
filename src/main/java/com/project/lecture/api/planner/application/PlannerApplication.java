package com.project.lecture.api.planner.application;

import com.project.lecture.api.planner.dto.PlannerDto;
import com.project.lecture.api.planner.dto.PlannerRequest.Update;
import com.project.lecture.api.planner.service.PlannerService;
import com.project.lecture.api.user.service.MemberService;
import com.project.lecture.entity.Member;
import com.project.lecture.entity.Planner;
import com.project.lecture.exception.kind.ExceptionCompletePlanner;
import com.project.lecture.exception.kind.ExceptionNotFoundPlanner;
import com.project.lecture.exception.kind.ExceptionNotValidUser;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.TypeRequest.Create;
import com.project.lecture.type.adapter.TypeAdapter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannerApplication {

  private final boolean FALSE_FLAG = false;
  private final boolean TRUE_FLAG = true;
  private final PlannerService plannerService;
  private final MemberService memberService;

  private final List<TypeAdapter> typeAdapters;
  private Map<StudyType, TypeAdapter> typeAdapterMap;

  @PostConstruct
  public void setUp(){
    typeAdapterMap = new HashMap<>();

    for (TypeAdapter adapter : typeAdapters) {
      StudyType studyType = adapter.getStudyType();
      typeAdapterMap.put(studyType, adapter);
    }
  }
  public void createPlannerByRequestAndEmail(Create request, String email) {
    if (request.getPlannerDt() == null) {
      throw new NullPointerException("날짜가 존재하지 않습니다.");
    }

    Member member = memberService.getMemberByEmail(email);

    TypeAdapter adapter = typeAdapterMap.get(request.getType());

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
  @Transactional
  public List<PlannerDto> getMonthlyPlannerByRequestAndEmail(LocalDate currentDate, String email) {
    LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
    LocalDate firstDayOfNextMonth = currentDate.plusMonths(1).withDayOfMonth(1);
    LocalDate lastDayOfNextMonth = firstDayOfNextMonth.withDayOfMonth(firstDayOfNextMonth.lengthOfMonth());
    List<Planner> planners = plannerService
        .getPlannersByNotComplete(firstDayOfMonth, lastDayOfNextMonth, email,FALSE_FLAG);

    // 오늘과 약속한 날의 이전의 약속날 과의 차이
    if (ChronoUnit.DAYS.between(currentDate, planners.get(0).getPlannerDt()) > 0) {
      return planners.stream().map(PlannerDto::toDto).collect(Collectors.toList());
    }

    // init
    LocalDate changeDt = LocalDate.now();
    LocalDate beforeDate = planners.get(0).getPlannerDt();
    planners.get(0).changeDate(changeDt);
    for (int i = 1; i < planners.size(); i++) {
      Planner planner = planners.get(i);
      Planner previousPlanner = planners.get(i - 1);
      // 같은 값인 경우
      if (planner.getPlannerDt().isEqual(beforeDate)) {
        planner.changeDate(changeDt);
      }
      // 현재 날짜보다 작거나 같은 경우는 or 이전 값이 현재 값보다 작을 경우
      else if (planner.getPlannerDt().compareTo(currentDate) <= 0 ||
          previousPlanner.getPlannerDt().isAfter(planner.getPlannerDt())) {
        changeDt = planners.get(i - 1).getPlannerDt().plusDays(1);
        beforeDate = planner.getPlannerDt();
        planner.changeDate(changeDt);
      }
      // 만약 이전 index 날짜와 현재 index 날짜가 같은 경우는 값을 1씩 증가하고 값을 갱신
      else if (previousPlanner.getPlannerDt().isEqual(planner.getPlannerDt())) {
        beforeDate = previousPlanner.getPlannerDt();
        changeDt = beforeDate.plusDays(1);
        planner.plusDay(1);
      } else {
        break;
      }
    }

    return planners.stream().map(PlannerDto::toDto).collect(Collectors.toList());
  }


  @Transactional
  public void completePlannerByIdAndEmail(Long id, String email) {
    Planner planner = plannerService.getPlannerById(id);

    if (!planner.getMember().getEmail().equals(email)){
      throw new ExceptionNotValidUser();
    }

    if (planner.isPlannerComplete()) {
      throw new ExceptionCompletePlanner();
    }
    planner.changeComplete();

    Member member = planner.getMember();
    TypeAdapter typeAdapter = typeAdapterMap.get(planner.getPlannerType());
    typeAdapter.complete(planner.getPlannerTypeId(), member);
  }
}
