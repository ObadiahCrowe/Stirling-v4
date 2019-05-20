package com.stirlinglms.stirling.entity.classroom.assignment;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.assignment.AssignmentBaseDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.AssignmentRepository;
import com.stirlinglms.stirling.service.AssignmentService;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;

@Immutable
@Entity(name = "assignments")
public class Assignment implements SaveableEntity<Assignment, AssignmentBaseDto, AssignmentService, AssignmentRepository> {

    @Id
    private Long id;

    @Reference
    private User poster;

    private String title;
    private String description;

    private boolean formative;

    private Instant createdOn;
    private Instant editedOn;
    private Instant dueOn;

    @Reference
    private Set<Resource> attachedResources;

    @Reference
    private Set<AssignmentUserData> assignmentUsers;

    @Deprecated
    private Assignment() {}

    public Assignment(User poster, String title, String description, boolean formative, Instant dueOn) {
        this.poster = poster;
        this.title = title;
        this.description = description;

        this.formative = formative;

        this.createdOn = Instant.now();
        this.editedOn = Instant.now();
        this.dueOn = dueOn;

        this.attachedResources = Sets.newConcurrentHashSet();
        this.assignmentUsers = Sets.newConcurrentHashSet();
    }

    public Assignment(User poster, String title, String description, boolean formative, Instant dueOn, Set<Resource> attachedResources) {
        this.poster = poster;
        this.title = title;
        this.description = description;

        this.formative = formative;

        this.createdOn = Instant.now();
        this.editedOn = Instant.now();
        this.dueOn = dueOn;

        this.attachedResources = attachedResources;
        this.assignmentUsers = Sets.newConcurrentHashSet();
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

    public String getDesc() {
        return this.description;
    }

    public boolean isFormative() {
        return this.formative;
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

    public Set<Resource> getAttachedResources() {
        return this.attachedResources;
    }

    public Set<AssignmentUserData> getAssignmentUsers() {
        return this.assignmentUsers;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public void setFormative(boolean formative) {
        this.formative = formative;
    }

    public void setEditedOn(Instant editedOn) {
        this.editedOn = editedOn;
    }

    public void setDueOn(Instant dueOn) {
        this.dueOn = dueOn;
    }

    public void setAttachedResources(Set<Resource> attachedResources) {
        this.attachedResources = attachedResources;
    }

    public void setAssignmentUsers(Set<AssignmentUserData> assignmentUsers) {
        this.assignmentUsers = assignmentUsers;
    }

    @Override
    public AssignmentService getService() {
        return Stirling.get().getAssignmentService();
    }

    @Override
    public AssignmentBaseDto getDto() {
        return new AssignmentBaseDto(this);
    }
}
