package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.repository.SchoolRepository;

public interface SchoolService extends RepositoryService<School, SchoolRepository> {

    School createSchool(String name, String location);

    School createSchool(String name, String location, String phoneNumber, String emailAddress);

    School getByName(String name);
}
