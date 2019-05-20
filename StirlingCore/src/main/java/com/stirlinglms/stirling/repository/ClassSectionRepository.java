package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassSection;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface ClassSectionRepository extends DatastoreRepository<ClassSection, Long> {

    List<ClassSection> findAllByTitle(String title);

    List<ClassSection> findAllByClassroom(Classroom classroom);
}
