package com.stirlinglms.stirling.entity.classroom;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.ClassroomDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.classroom.info.ClassSection;
import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ClassroomRepository;
import com.stirlinglms.stirling.service.ClassroomService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import java.util.Collections;
import java.util.Set;

@Entity(name = "classrooms")
public class Classroom implements SaveableEntity<Classroom, ClassroomDto, ClassroomService, ClassroomRepository> {

    @Id
    private Long id;

    private String name;
    private String room;

    @Reference
    private TimeSlot timeSlot;

    @Reference
    private Resource icon;

    @Reference
    private Resource banner;

    @Reference
    private School school;

    @Reference
    private Set<User> teachers;

    @Reference
    private Set<User> students;

    @Reference
    private Set<Lesson> lessons;

    @Reference
    private Set<ClassSection> sections;

    @Reference
    private Set<ClassPost> posts;

    @Reference
    private Set<ClassHomework> homework;

    @Reference
    private Set<Assignment> assignments;

    @Reference
    private Set<Resource> resources;

    @Deprecated
    private Classroom() {}

    public Classroom(User teacher, String name, String room, TimeSlot slot, School school) {
        this.name = name;
        this.room = room;
        this.icon = null;
        this.banner = null;
        this.timeSlot = slot;

        this.school = school;

        this.teachers = Sets.newConcurrentHashSet(Collections.singletonList(teacher));
        this.students = Sets.newConcurrentHashSet();

        this.lessons = Sets.newConcurrentHashSet();
        this.sections = Sets.newConcurrentHashSet();

        this.posts = Sets.newConcurrentHashSet();
        this.homework = Sets.newConcurrentHashSet();
        this.assignments = Sets.newConcurrentHashSet();
        this.resources = Sets.newConcurrentHashSet();
    }

    public Classroom(User teacher, String name, String room, TimeSlot slot, School school, Resource icon, Resource banner) {
        this.name = name;
        this.room = room;
        this.icon = icon;
        this.banner = banner;
        this.timeSlot = slot;

        this.school = school;

        this.teachers = Sets.newConcurrentHashSet(Collections.singletonList(teacher));
        this.students = Sets.newConcurrentHashSet();

        this.lessons = Sets.newConcurrentHashSet();
        this.sections = Sets.newConcurrentHashSet();

        this.posts = Sets.newConcurrentHashSet();
        this.homework = Sets.newConcurrentHashSet();
        this.assignments = Sets.newConcurrentHashSet();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getRoom() {
        return this.room;
    }

    public Resource getIcon() {
        return this.icon;
    }

    public Resource getBanner() {
        return this.banner;
    }

    public TimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public School getSchool() {
        return this.school;
    }

    public Set<User> getTeachers() {
        return Collections.unmodifiableSet(this.teachers);
    }

    public Set<User> getStudents() {
        return Collections.unmodifiableSet(this.students);
    }

    public Set<Lesson> getLessons() {
        return Collections.unmodifiableSet(this.lessons);
    }

    public Set<ClassSection> getSections() {
        return Collections.unmodifiableSet(this.sections);
    }

    public Set<ClassPost> getPosts() {
        return Collections.unmodifiableSet(this.posts);
    }

    public Set<ClassHomework> getHomework() {
        return Collections.unmodifiableSet(this.homework);
    }

    public Set<Assignment> getAssignments() {
        return Collections.unmodifiableSet(this.assignments);
    }

    public Set<Resource> getResources() {
        return Collections.unmodifiableSet(this.resources);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setIcon(Resource icon) {
        this.icon = icon;
    }

    public void setBanner(Resource banner) {
        this.banner = banner;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setTeachers(Set<User> teachers) {
        this.teachers = teachers;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setSections(Set<ClassSection> sections) {
        this.sections = sections;
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
    public ClassroomService getService() {
        return Stirling.get().getClassroomService();
    }

    @Override
    public ClassroomDto getDto() {
        return new ClassroomDto(this);
    }
}
