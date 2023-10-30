package com.project.lecture.Listen.service;

import com.project.lecture.entity.Listening;
import com.project.lecture.repository.ListeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ListenService {

  private final ListeningRepository listeningRepository;

  public boolean existCheck(Long courseId, Long memberId) {
    return listeningRepository.existsByMemberIdAndCourseId(memberId, courseId) >= 1;
  }

  public void saveListening(Listening listening) {
    listeningRepository.save(listening);
  }

  public void deleteListing(Long courseId, Long memberId) {
    listeningRepository.deleteByMemberIdAndCourseId(memberId, courseId);
  }
}
