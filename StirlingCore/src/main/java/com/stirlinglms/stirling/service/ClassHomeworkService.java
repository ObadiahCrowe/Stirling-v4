package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassHomeworkRepository;

import java.time.Instant;
import java.util.Set;

public interface ClassHomeworkService extends RepositoryService<ClassHomework, ClassHomeworkRepository> {

    ClassHomework createHomework(User poster, String title, String content, Instant dueOn);

    ClassHomework createHomework(User poster, String title, String content, Instant dueOn, Set<Resource> resources);

    Set<ClassHomework> getByPoster(User user);

    Set<ClassHomework> getByClass(Classroom classroom);

    Set<ClassHomework> getByPosterAndClass(Classroom classroom, User user);
}
