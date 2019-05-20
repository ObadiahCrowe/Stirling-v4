package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.repository.LessonRepository;

public interface LessonService extends RepositoryService<Lesson, LessonRepository> {

    Lesson createLesson(Classroom classroom, TimeSlot timeSlot);

}
