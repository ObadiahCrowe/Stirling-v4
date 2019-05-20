package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.dto.assignment.AssignmentBaseDto;
import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.classroom.info.ClassSection;
import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import com.stirlinglms.stirling.entity.user.User;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class ClassroomDto {

    private final long id;
    private final String name;
    private final String room;

    private final ResourceDto icon;
    private final ResourceDto banner;

    private final TimeSlotDto timeSlot;
    private final SchoolDto school;

    private final Set<UserDto> teachers;
    private final Set<UserDto> students;

    private final Set<LessonDto> lessons;
    private final Set<ClassSectionDto> sections;
    private final Set<ClassPostDto> posts;
    private final Set<ClassHomeworkDto> homework;
    private final Set<AssignmentBaseDto> assignments;
    
    public ClassroomDto(Classroom classroom) {
        this.id = classroom.getId();
        this.name = classroom.getName();
        this.room = classroom.getRoom();

        this.icon = classroom.getIcon().getDto();
        this.banner = classroom.getBanner().getDto();

        this.timeSlot = classroom.getTimeSlot().getDto();
        this.school = classroom.getSchool().getDto();

        this.teachers = classroom.getTeachers().stream().map(User::getDto).collect(Collectors.toSet());
        this.students = classroom.getStudents().stream().map(User::getDto).collect(Collectors.toSet());

        this.lessons = classroom.getLessons().stream().map(Lesson::getDto).collect(Collectors.toSet());
        this.sections = classroom.getSections().stream().map(ClassSection::getDto).collect(Collectors.toSet());
        this.posts = classroom.getPosts().stream().map(ClassPost::getDto).collect(Collectors.toSet());

        this.homework = classroom.getHomework().stream().map(ClassHomework::getDto).collect(Collectors.toSet());
        this.assignments = classroom.getAssignments().stream().map(Assignment::getDto).collect(Collectors.toSet());
    }
}
