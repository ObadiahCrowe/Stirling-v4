package com.stirlinglms.stirling.entity.classroom.info;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.ClassHomeworkDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassHomeworkRepository;
import com.stirlinglms.stirling.service.ClassHomeworkService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Immutable
@Entity(name = "homework")
public class ClassHomework implements SaveableEntity<ClassHomework, ClassHomeworkDto, ClassHomeworkService, ClassHomeworkRepository> {

    @Id
    private Long id;

    @Reference
    private User poster;

    private String title;
    private String content;

    private Instant createdOn;
    private Instant editedOn;
    private Instant dueOn;

    @Reference
    private Set<Resource> resources;

    @Deprecated
    private ClassHomework() {}

    public ClassHomework(User poster, String title, String content, Instant dueOn) {
        this.poster = poster;

        this.title = title;
        this.content = content;

        this.createdOn = Instant.now();
        this.editedOn = Instant.now();
        this.dueOn = dueOn;

        this.resources = Sets.newConcurrentHashSet();
    }

    public ClassHomework(User poster, String title, String content, Instant dueOn, Set<Resource> resources) {
        this.poster = poster;

        this.title = title;
        this.content = content;

        this.createdOn = Instant.now();
        this.editedOn = Instant.now();
        this.dueOn = dueOn;

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

    public Instant getDueOn() {
        return this.dueOn;
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

    public void setDueOn(Instant dueOn) {
        this.dueOn = dueOn;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public ClassHomeworkService getService() {
        return Stirling.get().getHomeworkService();
    }

    @Override
    public ClassHomeworkDto getDto() {
        return new ClassHomeworkDto(this);
    }
}
