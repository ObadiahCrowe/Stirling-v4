package com.stirlinglms.stirling.entity.classroom.info;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.ClassPostDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassPostRepository;
import com.stirlinglms.stirling.service.ClassPostService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Immutable
@Entity(name = "posts")
public class ClassPost implements SaveableEntity<ClassPost, ClassPostDto, ClassPostService, ClassPostRepository> {

    @Id
    private Long id;

    @Reference
    private User poster;

    private String title;
    private String content;

    private Instant createdOn;
    private Instant editedOn;

    @Reference
    private Set<Resource> resources;

    @Deprecated
    private ClassPost() {}

    public ClassPost(User poster, String title, String content) {
        this.poster = poster;

        this.title = title;
        this.content = content;

        this.createdOn = Instant.now();
        this.editedOn = Instant.now();

        this.resources = Sets.newConcurrentHashSet();
    }

    public ClassPost(User poster, String title, String content, Set<Resource> resources) {
        this.poster = poster;

        this.title = title;
        this.content = content;

        this.createdOn = Instant.now();
        this.editedOn = Instant.now();

        this.resources = resources;
    }

    public Long getId() {
        return this.id;
    }

    public User getPoster() {
        return this.poster;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Instant getEditedOn() {
        return this.editedOn;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEditedOn(Instant editedOn) {
        this.editedOn = editedOn;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public ClassPostService getService() {
        return Stirling.get().getPostService();
    }

    @Override
    public ClassPostDto getDto() {
        return new ClassPostDto(this);
    }
}
