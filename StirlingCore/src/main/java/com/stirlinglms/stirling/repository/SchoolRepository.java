package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.school.School;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface SchoolRepository extends DatastoreRepository<School, Long> {

    School findByName(String name);
}
