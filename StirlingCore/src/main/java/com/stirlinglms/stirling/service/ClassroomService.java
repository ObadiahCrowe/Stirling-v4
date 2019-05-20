package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassroomRepository;

import java.util.Set;

public interface ClassroomService extends RepositoryService<Classroom, ClassroomRepository> {

    Classroom createClassroom(User teacher, String name, String room, TimeSlot timeSlot, School school);

    Classroom createClassroom(User teacher, String name, String room, TimeSlot timeSlot, School school, Resource icon, Resource banner);

    Set<Classroom> getByName(String name);

    Set<Classroom> getByTimeSlot(TimeSlot slot);

    Set<Classroom> getBySchool(School school);

    Set<Classroom> getByTeacher(User teacher);

    Set<Classroom> getByStudent(User student);
}
