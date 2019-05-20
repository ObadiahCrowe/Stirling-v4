package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface TimeSlotRepository extends DatastoreRepository<TimeSlot, Long> {}
