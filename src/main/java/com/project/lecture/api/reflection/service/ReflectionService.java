package com.project.lecture.api.reflection.service;

import com.project.lecture.entity.Reflection;
import com.project.lecture.repository.ReflectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReflectionService {

  private final ReflectionRepository reflectionRepository;

  public void createByEntity(Reflection reflection) {
    reflectionRepository.save(reflection);
  }

  public boolean existsByIdAndEmail(Long id, String email) {
    return reflectionRepository
        .existsByReflectionIdAndMember_Email(id,email);
  }

  public void deleteById(Long id) {
    reflectionRepository.deleteById(id);
  }
}
