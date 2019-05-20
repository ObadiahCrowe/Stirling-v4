package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassHomeworkRepository;
import com.stirlinglms.stirling.service.ClassHomeworkService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassHomeworkServiceImpl implements ClassHomeworkService {

    private static final String[] VALID_FIELDS = new String[] {
      "title",
      "content",
      "dueOn",
      "resources"
    };

    private final ClassHomeworkRepository repository;

    @Autowired
    ClassHomeworkServiceImpl(ClassHomeworkRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public ClassHomework createHomework(User poster, String title, String content, Instant dueOn) {
        ClassHomework homework = new ClassHomework(poster, title, content, dueOn);

        this.repository.save(homework);

        return homework;
    }

    @Override
    public ClassHomework createHomework(User poster, String title, String content, Instant dueOn, Set<Resource> resources) {
        ClassHomework homework = new ClassHomework(poster, title, content, dueOn, resources);

        this.repository.save(homework);

        return homework;
    }

    @Override
    public Set<ClassHomework> getByPoster(User user) {
        return this.repository.findAllByPoster(user).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<ClassHomework> getByClass(Classroom classroom) {
        return classroom.getHomework().stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<ClassHomework> getByPosterAndClass(Classroom classroom, User user) {
        return classroom.getHomework().stream().filter(c -> c.getPoster().getUuid() == user.getUuid()).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public ClassHomework update(ClassHomework entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public ClassHomework delete(ClassHomework entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public ClassHomework getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<ClassHomework> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public ClassHomeworkRepository getRepository() {
        return this.repository;
    }
}
