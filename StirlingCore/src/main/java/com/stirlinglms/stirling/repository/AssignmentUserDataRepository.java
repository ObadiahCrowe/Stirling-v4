package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.classroom.assignment.AssignmentUserData;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;
import java.util.UUID;

public interface AssignmentUserDataRepository extends DatastoreRepository<AssignmentUserData, Long> {

    AssignmentUserData findByUuid(UUID uuid);

    List<AssignmentUserData> findAllByUser(User user);
}
