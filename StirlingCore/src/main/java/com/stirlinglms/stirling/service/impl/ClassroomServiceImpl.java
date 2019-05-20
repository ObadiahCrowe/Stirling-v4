package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassroomRepository;
import com.stirlinglms.stirling.service.ClassroomService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private static final String[] VALID_FIELDS = new String[] {
      "name",
      "room",
      "icon",
      "banner",
      "timeSlot",
      "teachers",
      "students",
      "lessons",
      "sections",
      "posts",
      "homework",
      "assignments"
    };

    private final ClassroomRepository repository;

    @Autowired
    ClassroomServiceImpl(ClassroomRepository repository) {
        this.repository = repository;
    }

    @Override
    public Classroom createClassroom(User teacher, String name, String room, TimeSlot timeSlot, School school) {
        Classroom classroom = new Classroom(teacher, name, room, timeSlot, school);

        this.repository.save(classroom);

        return classroom;
    }

    @Override
    public Classroom createClassroom(User teacher, String name, String room, TimeSlot timeSlot, School school, Resource icon, Resource banner) {
        Classroom classroom = new Classroom(teacher, name, room, timeSlot, school, icon, banner);

        this.repository.save(classroom);

        return classroom;
    }

    @Override
    public Set<Classroom> getByName(String name) {
        return this.repository.findAllByName(name).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Classroom> getByTimeSlot(TimeSlot slot) {
        return this.repository.findAllByTimeSlot(slot).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Classroom> getBySchool(School school) {
        return this.repository.findAllBySchool(school).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Classroom> getByTeacher(User teacher) {
        return this.repository.findAllByTeachersContains(teacher).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Classroom> getByStudent(User student) {
        return this.repository.findAllByStudentsContains(student).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Classroom update(Classroom entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public Classroom delete(Classroom entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public Classroom getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<Classroom> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public ClassroomRepository getRepository() {
        return this.repository;
    }
}
