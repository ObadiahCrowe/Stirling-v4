package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface AssignmentRepository extends DatastoreRepository<Assignment, Long> {

    List<Assignment> findAllByPoster(User poster);

    List<Assignment> findAllByTitle(String title);

    List<Assignment> findAllByFormative(boolean formative);
}
