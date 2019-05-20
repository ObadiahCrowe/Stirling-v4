package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.repository.LessonRepository;
import com.stirlinglms.stirling.service.LessonService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LessonServiceImpl implements LessonService {

    private static final String[] VALID_FIELDS = new String[] {
      "attendance",
      "posts",
      "homework",
      "resources"
    };

    private final LessonRepository repository;

    @Autowired
    LessonServiceImpl(LessonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Lesson createLesson(Classroom classroom, TimeSlot timeSlot) {
        Lesson lesson = new Lesson(classroom, timeSlot);

        this.repository.save(lesson);

        return lesson;
    }

    @Override
    public Lesson update(Lesson entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public Lesson delete(Lesson entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public Lesson getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<Lesson> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public LessonRepository getRepository() {
        return this.repository;
    }
}
