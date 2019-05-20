package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassPostRepository;
import com.stirlinglms.stirling.service.ClassPostService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassPostServiceImpl implements ClassPostService {

    private static final String[] VALID_FIELDS = new String[] {
      "title",
      "content",
      "resources"
    };

    private final ClassPostRepository repository;

    @Autowired
    ClassPostServiceImpl(ClassPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClassPost createPost(User poster, String title, String content) {
        ClassPost post = new ClassPost(poster, title, content);

        this.repository.save(post);

        return post;
    }

    @Override
    public ClassPost createPost(User poster, String title, String content, Set<Resource> resources) {
        ClassPost post = new ClassPost(poster, title, content, resources);

        this.repository.save(post);

        return post;
    }

    @Override
    public Set<ClassPost> getByPoster(User user) {
        return this.repository.findAllByPoster(user).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<ClassPost> getByClass(Classroom classroom) {
        return classroom.getPosts().stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<ClassPost> getByPosterAndClass(Classroom classroom, User user) {
        return classroom.getPosts().stream().filter(p -> p.getPoster().getUuid() == user.getUuid()).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public ClassPost update(ClassPost entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public ClassPost delete(ClassPost entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public ClassPost getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<ClassPost> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public ClassPostRepository getRepository() {
        return this.repository;
    }
}
