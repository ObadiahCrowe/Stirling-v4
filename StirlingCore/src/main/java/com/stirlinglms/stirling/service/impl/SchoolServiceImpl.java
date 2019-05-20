package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.repository.SchoolRepository;
import com.stirlinglms.stirling.service.SchoolService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SchoolServiceImpl implements SchoolService {

    private static final String[] VALID_FIELDS = new String[] {
      "name",
      "location",
      "phoneNumber",
      "emailAddress",
      "primaryPlatform",
      "platformSources",
      "timeSlots"
    };

    private final SchoolRepository repository;

    @Autowired
    SchoolServiceImpl(SchoolRepository repository) {
        this.repository = repository;
    }

    @Override
    public School createSchool(String name, String location) {
        if (this.getByName(name) != null) {
            throw new IllegalArgumentException("School with the name, " + name + ", already exists!");
        }

        School school = new School(name, location);

        this.repository.save(school);

        return school;
    }

    @Override
    public School createSchool(String name, String location, String phoneNumber, String emailAddress) {
        if (this.getByName(name) != null) {
            throw new IllegalArgumentException("School with the name, " + name + ", already exists!");
        }

        School school = new School(name, location, phoneNumber, emailAddress);

        this.repository.save(school);

        return school;
    }

    @Override
    public School getByName(String name) {
        return this.getAll().stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public School update(School entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public School delete(School entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public School getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<School> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public SchoolRepository getRepository() {
        return this.repository;
    }
}
