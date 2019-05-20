package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface LessonRepository extends DatastoreRepository<Lesson, Long> {}
