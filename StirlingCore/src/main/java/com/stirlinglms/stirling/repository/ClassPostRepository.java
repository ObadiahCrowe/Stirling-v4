package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface ClassPostRepository extends DatastoreRepository<ClassPost, Long> {

    List<ClassPost> findAllByPoster(User user);
}
