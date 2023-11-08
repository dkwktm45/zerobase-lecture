package com.project.lecture.api.complete.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private final ObjectMapper objectMapper;
  public void saveEntity(MemberCourseLecture memberCourseLecture) {
    memberCourseRepository.save(memberCourseLecture);
  }

  public void completePlusTierByEmailAndTime(String email, int totalTime) {
    String stringTypeUserTier = userTierClient.get(email);
    try {

      if (stringTypeUserTier == null) {
        userTierClient.put(email,
            objectMapper.writeValueAsString(new UserTier(totalTime))
        );
      } else {
        UserTier userTier = objectMapper.readValue(stringTypeUserTier, UserTier.class);
        userTier.plusTime(totalTime);

        userTierClient.put(email,objectMapper.writeValueAsString(userTier));
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  public boolean existCourseIdByMemberAndId(Member member, Long courseId) {
    return memberCourseRepository.existsByCourseIdAndMember(courseId,member);
  }

  public MemberCourseLecture getCourseLectureByMemberAndId(Member member, Long courseId) {
    return memberCourseRepository.findByMemberAndCourseId(member,courseId);
  }


}
