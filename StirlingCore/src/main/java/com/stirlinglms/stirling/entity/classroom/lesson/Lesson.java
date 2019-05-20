package com.stirlinglms.stirling.entity.classroom.lesson;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.LessonDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.classroom.AttendanceStatus;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.LessonRepository;
import com.stirlinglms.stirling.service.LessonService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Immutable
@Entity(name = "lessons")
public class Lesson implements SaveableEntity<Lesson, LessonDto, LessonService, LessonRepository> {

    @Id
    private Long id;

    @Reference
    private Classroom classroom;

    @Reference
    private TimeSlot timeSlot;

    @Reference
    private Map<User, AttendanceStatus> attendance; // Student database id, whether they attended.

    @Reference
    private Set<ClassPost> posts;

    @Reference
    private Set<ClassHomework> homework;

    @Reference
    private Set<Resource> resources;

    @Deprecated
    private Lesson() {}

    public Lesson(Classroom classroom, TimeSlot timeSlot) {
        this.classroom = classroom;
        this.timeSlot = timeSlot;
        this.attendance = classroom.getStudents().stream()
          .collect(Collectors.toConcurrentMap((u) -> u, (u) -> AttendanceStatus.ROLL_NOT_MARKED));

        this.posts = Sets.newConcurrentHashSet();
        this.homework = Sets.newConcurrentHashSet();
        this.resources = Sets.newConcurrentHashSet();
    }

    public Long getId() {
        return this.id;
    }

    public Classroom getClassroom() {
        return this.classroom;
    }

    public TimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public Map<User, AttendanceStatus> getAttendance() {
        return this.attendance;
    }

    public Set<ClassPost> getPosts() {
        return this.posts;
    }

    public Set<ClassHomework> getHomework() {
        return this.homework;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setAttendance(Map<User, AttendanceStatus> attendance) {
        this.attendance = attendance;
    }

    public void setPosts(Set<ClassPost> posts) {
        this.posts = posts;
    }

    public void setHomework(Set<ClassHomework> homework) {
        this.homework = homework;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public LessonService getService() {
        return Stirling.get().getLessonService();
    }

    @Override
    public LessonDto getDto() {
        return new LessonDto(this);
    }
}
