package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.res.Resource;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface ResourceRepository extends DatastoreRepository<Resource, Long> {}
