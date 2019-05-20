package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassPostRepository;

import java.util.Set;

public interface ClassPostService extends RepositoryService<ClassPost, ClassPostRepository> {

    ClassPost createPost(User poster, String title, String content);

    ClassPost createPost(User poster, String title, String content, Set<Resource> resources);

    Set<ClassPost> getByPoster(User user);

    Set<ClassPost> getByClass(Classroom classroom);

    Set<ClassPost> getByPosterAndClass(Classroom classroom, User user);
}
