package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.AssignmentRepository;

import java.time.Instant;
import java.util.Set;

public interface AssignmentService extends RepositoryService<Assignment, AssignmentRepository> {

    Assignment createAssignment(User user, String title, String description, boolean formative, Instant dueOn);

    Assignment createAssignment(User user, String title, String description, boolean formative, Instant dueOn, Set<Resource> resources);

    Set<Assignment> getByPoster(User poster);

    Set<Assignment> getByTitle(String title);

    Set<Assignment> getByFormative(boolean formative);
}
