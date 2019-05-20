package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.assignment.AssignmentUserData;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.AssignmentUserDataRepository;

import java.util.Set;
import java.util.UUID;

public interface AssignmentUserDataService extends RepositoryService<AssignmentUserData, AssignmentUserDataRepository> {

    AssignmentUserData createAssignmentUser(User user);

    AssignmentUserData getByUuid(UUID uuid);

    Set<AssignmentUserData> getByUser(User user);

    Set<AssignmentUserData> getByAssignment(Assignment assignment);

    Set<AssignmentUserData> getByAssignmentAndUser(Assignment assignment, User user);
}
