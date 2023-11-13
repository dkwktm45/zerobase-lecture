package com.project.lecture.config;

import com.project.lecture.api.Listen.service.ListenService;
import com.project.lecture.api.complete.service.CourseLectureService;
import com.project.lecture.api.course.service.CourseService;
import com.project.lecture.api.course.service.LectureService;
import com.project.lecture.api.reminder.service.ReminderService;
import com.project.lecture.api.study.service.StudyService;
import com.project.lecture.redis.LectureClient;
import com.project.lecture.type.StudyType;
import com.project.lecture.type.adapter.CourseAdapter;
import com.project.lecture.type.adapter.LectureAdapter;
import com.project.lecture.type.adapter.ReminderAdapter;
import com.project.lecture.type.adapter.StudyAdapter;
import com.project.lecture.type.adapter.TypeAdapter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterConfig {

  private final StudyService studyService;
  private final LectureService lectureService;
  private final CourseService courseService;
  private final ListenService listenService;
  private final CourseLectureService courseLectureService;
  private final LectureClient lectureClient;
  private final ReminderService reminderService;

  public AdapterConfig(StudyService studyService,
      LectureService lectureService, CourseService courseService, ListenService listenService,
      CourseLectureService courseLectureService, LectureClient lectureClient,
      ReminderService reminderService) {
    this.studyService = studyService;
    this.lectureService = lectureService;
    this.courseService = courseService;
    this.listenService = listenService;
    this.courseLectureService = courseLectureService;
    this.lectureClient = lectureClient;
    this.reminderService = reminderService;
  }

  @Bean
  public Map<StudyType, TypeAdapter> adapterMap(){
    Map<StudyType, TypeAdapter> adapters = new HashMap<>();
    adapters.put(StudyType.STUDY, new StudyAdapter(studyService));
    adapters.put(StudyType.COURSE, new CourseAdapter(
        listenService,
        courseService,
        courseLectureService,
        lectureClient));
    adapters.put(StudyType.LECTURE, new LectureAdapter(
        listenService,
        lectureService,
        courseService,
        lectureClient,
        courseLectureService
        ));
    adapters.put(StudyType.REMINDER, new ReminderAdapter(reminderService));
    return adapters;
  }
}
