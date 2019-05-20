package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.credential.Credential;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface CredentialRepository extends DatastoreRepository<Credential, Long> {}
