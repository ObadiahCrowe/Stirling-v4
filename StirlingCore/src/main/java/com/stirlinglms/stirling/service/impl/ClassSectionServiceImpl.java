package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassSection;
import com.stirlinglms.stirling.repository.ClassSectionRepository;
import com.stirlinglms.stirling.service.ClassSectionService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassSectionServiceImpl implements ClassSectionService {

    private static final String[] VALID_FIELDS = new String[] {
      "title",
      "description",
      "posts",
      "homework",
      "assignments"
    };

    private final ClassSectionRepository repository;

    @Autowired
    ClassSectionServiceImpl(ClassSectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClassSection create(Classroom classroom, String title) {
        ClassSection section = new ClassSection(classroom, title);

        this.repository.save(section);

        return section;
    }

    @Override
    public ClassSection create(Classroom classroom, String title, String description) {
        ClassSection section = new ClassSection(classroom, title, description);

        this.repository.save(section);

        return section;
    }

    @Override
    public Set<ClassSection> getByTitle(String title) {
        return this.repository.findAllByTitle(title).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<ClassSection> getByClassroom(Classroom classroom) {
        return this.repository.findAllByClassroom(classroom).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public ClassSection update(ClassSection entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public ClassSection delete(ClassSection entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public ClassSection getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<ClassSection> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public ClassSectionRepository getRepository() {
        return this.repository;
    }
}
