package com.project.lecture.api.Listen.service;

import com.project.lecture.entity.Course;
import com.project.lecture.entity.Listening;
import com.project.lecture.entity.Member;
import com.project.lecture.repository.ListeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ListenService {

  private final ListeningRepository listeningRepository;

  public boolean existCheck(Long courseId, Long memberId) {
    return listeningRepository.existsByMember_MemberIdAndCourse_CourseId(memberId, courseId);
  }
  @Transactional
  public void saveListening(Listening listening) {
    listeningRepository.save(listening);
  }
  @Transactional
  public void deleteListing(Long courseId, Long memberId) {
    listeningRepository.deleteByMemberIdAndCourseId(memberId, courseId);
  }

  public boolean existCheckByMemberAndCourse(Member member, Course course) {
    return listeningRepository.existsByMemberAndCourse(member, course);
  }
}
