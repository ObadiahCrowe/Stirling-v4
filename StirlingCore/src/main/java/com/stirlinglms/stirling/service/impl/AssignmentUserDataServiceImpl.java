package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.assignment.AssignmentUserData;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.AssignmentUserDataRepository;
import com.stirlinglms.stirling.service.AssignmentUserDataService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssignmentUserDataServiceImpl implements AssignmentUserDataService {

    private static final String[] VALID_FIELDS = new String[] {
      "result",
      "submittedResources",
      "assignedOn",
      "completedOn"
    };

    private final AssignmentUserDataRepository repository;

    @Autowired
    AssignmentUserDataServiceImpl(AssignmentUserDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public AssignmentUserData createAssignmentUser(User user) {
        AssignmentUserData data = new AssignmentUserData(user);

        this.repository.save(data);

        return data;
    }

    @Override
    public AssignmentUserData getByUuid(UUID uuid) {
        return this.repository.findByUuid(uuid);
    }

    @Override
    public Set<AssignmentUserData> getByUser(User user) {
        return this.repository.findAllByUser(user).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<AssignmentUserData> getByAssignment(Assignment assignment) {
        return Collections.unmodifiableSet(assignment.getAssignmentUsers());
    }

    @Override
    public Set<AssignmentUserData> getByAssignmentAndUser(Assignment assignment, User user) {
        return assignment.getAssignmentUsers().stream().filter(a -> a.getUser().getUuid() == user.getUuid()).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public AssignmentUserData update(AssignmentUserData entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public AssignmentUserData delete(AssignmentUserData entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public AssignmentUserData getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<AssignmentUserData> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public AssignmentUserDataRepository getRepository() {
        return this.repository;
    }
}
