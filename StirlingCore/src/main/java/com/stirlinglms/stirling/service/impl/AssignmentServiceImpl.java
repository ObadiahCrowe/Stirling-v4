package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.AssignmentRepository;
import com.stirlinglms.stirling.service.AssignmentService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private static final String[] VALID_FIELDS = new String[] {
      "title",
      "description",
      "formative",
      "dueOn",
      "attachedResources",
      "assignmentUsers"
    };

    private final AssignmentRepository repository;

    @Autowired
    AssignmentServiceImpl(AssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Assignment createAssignment(User user, String title, String description, boolean formative, Instant dueOn) {
        Assignment assignment = new Assignment(user, title, description, formative, dueOn);

        this.repository.save(assignment);

        return assignment;
    }

    @Override
    public Assignment createAssignment(User user, String title, String description, boolean formative, Instant dueOn, Set<Resource> resources) {
        Assignment assignment = new Assignment(user, title, description, formative, dueOn, resources == null ? Sets.newConcurrentHashSet() : resources);

        this.repository.save(assignment);

        return assignment;
    }

    @Override
    public Set<Assignment> getByPoster(User poster) {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAllByPoster(poster)));
    }

    @Override
    public Set<Assignment> getByTitle(String title) {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAllByTitle(title)));
    }

    @Override
    public Set<Assignment> getByFormative(boolean formative) {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAllByFormative(formative)));
    }

    @Override
    public Assignment update(Assignment entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public Assignment delete(Assignment entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public Assignment getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<Assignment> getAll() {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAll()));
    }

    @Override
    public AssignmentRepository getRepository() {
        return this.repository;
    }
}
