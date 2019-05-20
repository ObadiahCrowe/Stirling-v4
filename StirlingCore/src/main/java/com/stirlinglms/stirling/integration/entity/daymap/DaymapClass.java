package com.stirlinglms.stirling.integration.entity.daymap;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.integration.entity.ImportableClass;

import java.util.List;
import java.util.Set;

public class DaymapClass extends ImportableClass<Integer> {

    private volatile TimeSlot timeSlot;
    private volatile String room;

    private final Set<String> teachers;
    private final Set<Lesson> lessons;
    private final List<ClassPost> posts;
    private final List<ClassHomework> homework;

    public DaymapClass(String name, int id) {
        super(name, id);

        this.teachers = Sets.newConcurrentHashSet();
        this.lessons = Sets.newConcurrentHashSet();
        this.posts = Lists.newArrayList();
        this.homework = Lists.newArrayList();
    }

    public TimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public String getRoom() {
        return this.room;
    }

    public Set<String> getTeachers() {
        return this.teachers;
    }

    public Set<Lesson> getLessons() {
        return this.lessons;
    }

    public List<ClassPost> getPosts() {
        return this.posts;
    }

    public List<ClassHomework> getHomework() {
        return this.homework;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
