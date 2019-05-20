package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassSection;
import com.stirlinglms.stirling.repository.ClassSectionRepository;

import java.util.Set;

public interface ClassSectionService extends RepositoryService<ClassSection, ClassSectionRepository> {

    ClassSection create(Classroom classroom, String title);

    ClassSection create(Classroom classroom, String title, String description);

    Set<ClassSection> getByTitle(String title);

    Set<ClassSection> getByClassroom(Classroom classroom);
}
