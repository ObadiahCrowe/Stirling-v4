package com.stirlinglms.stirling.entity.classroom.info;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.ClassSectionDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.repository.ClassSectionRepository;
import com.stirlinglms.stirling.service.ClassSectionService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.Set;

@Immutable
@Entity(name = "sections")
public class ClassSection implements SaveableEntity<ClassSection, ClassSectionDto, ClassSectionService, ClassSectionRepository> {

    @Id
    private Long id;

    @Reference
    private Classroom classroom;

    private String title;
    private String description;

    @Reference
    private Set<ClassPost> posts;

    @Reference
    private Set<ClassHomework> homework;

    @Reference
    private Set<Assignment> assignments;

    @Reference
    private Set<Resource> resources;

    @Deprecated
    private ClassSection() {}

    public ClassSection(Classroom classroom, String title) {
        this.classroom = classroom;

        this.title = title;
        this.description = "";

        this.posts = Sets.newConcurrentHashSet();
        this.homework = Sets.newConcurrentHashSet();
        this.assignments = Sets.newConcurrentHashSet();
        this.resources = Sets.newConcurrentHashSet();
    }

    public ClassSection(Classroom classroom, String title, String description) {
        this.classroom = classroom;

        this.title = title;
        this.description = description;

        this.posts = Sets.newConcurrentHashSet();
        this.homework = Sets.newConcurrentHashSet();
        this.assignments = Sets.newConcurrentHashSet();
        this.resources = Sets.newConcurrentHashSet();
    }

    public Long getId() {
        return this.id;
    }

    public Classroom getClassroom() {
        return this.classroom;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.description;
    }

    public Set<ClassPost> getPosts() {
        return this.posts;
    }

    public Set<ClassHomework> getHomework() {
        return this.homework;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public void setPosts(Set<ClassPost> posts) {
        this.posts = posts;
    }

    public void setHomework(Set<ClassHomework> homework) {
        this.homework = homework;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public ClassSectionService getService() {
        return Stirling.get().getSectionService();
    }

    @Override
    public ClassSectionDto getDto() {
        return new ClassSectionDto(this);
    }
}
