package com.project.lecture.api.complete.service;

import com.project.lecture.entity.Member;
import com.project.lecture.entity.MemberCourseLecture;
import com.project.lecture.redis.UserTierClient;
import com.project.lecture.redis.dto.UserTier;
import com.project.lecture.repository.MemberCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseLectureService {

  private final MemberCourseRepository memberCourseRepository;
  private final UserTierClient userTierClient;

  public void saveEntity(MemberCourseLecture memberCourseLecture) {
    memberCourseRepository.save(memberCourseLecture);
  }

  public void completePlusTierByEmailAndTime(String email, int totalTime) {
    UserTier userTier = userTierClient.getUserTier(email);
    if (userTier == null) {
      userTierClient.putUserTier(email,
          new UserTier(totalTime)
      );
    } else {
      userTier.plusTime(totalTime);

      userTierClient.putUserTier(email, userTier);
    }
  }

  public boolean existCourseIdByMemberAndId(Member member, Long courseId) {
    return memberCourseRepository.existsByCourseIdAndMember(courseId, member);
  }

  public MemberCourseLecture getCourseLectureByMemberAndId(Member member, Long courseId) {
    return memberCourseRepository.findByMemberAndCourseId(member, courseId);
  }


}
