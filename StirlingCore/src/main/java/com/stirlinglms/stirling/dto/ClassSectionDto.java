package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.dto.assignment.AssignmentBaseDto;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.classroom.info.ClassSection;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class ClassSectionDto {

    private final long id;
    private final String title;
    private final String description;

    private final Set<ClassPostDto> posts;
    private final Set<ClassHomeworkDto> homework;
    private final Set<AssignmentBaseDto> assignments;

    public ClassSectionDto(ClassSection section) {
        this.id = section.getId();
        this.title = section. getTitle();
        this.description = section.getDesc();

        this.posts = section.getPosts().stream().map(ClassPost::getDto).collect(Collectors.toSet());
        this.homework = section.getHomework().stream().map(ClassHomework::getDto).collect(Collectors.toSet());
        this.assignments = section.getAssignments().stream().map(Assignment::getDto).collect(Collectors.toSet());
    }
}
