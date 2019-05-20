package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface ClassroomRepository extends DatastoreRepository<Classroom, Long> {

    List<Classroom> findAllByName(String name);

    List<Classroom> findAllByTimeSlot(TimeSlot slot);

    List<Classroom> findAllBySchool(School school);

    List<Classroom> findAllByTeachersContains(User user);

    List<Classroom> findAllByStudentsContains(User user);
}
