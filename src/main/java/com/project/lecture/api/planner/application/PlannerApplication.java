package com.project.lecture.api.planner.application;

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
import java.util.Map;
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

  private final Map<StudyType, TypeAdapter> adapters;

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
    TypeAdapter typeAdapter = adapters.get(planner.getPlannerType());
    typeAdapter.complete(planner.getPlannerTypeId(), member);
  }
}
