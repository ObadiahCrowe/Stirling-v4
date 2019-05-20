package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface ClassHomeworkRepository extends DatastoreRepository<ClassHomework, Long> {

    List<ClassHomework> findAllByPoster(User user);
}
