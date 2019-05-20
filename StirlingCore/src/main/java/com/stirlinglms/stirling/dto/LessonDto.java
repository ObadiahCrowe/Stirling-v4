package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.classroom.AttendanceStatus;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import com.stirlinglms.stirling.entity.res.Resource;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class LessonDto {

    private final long id;
    private final Map<String, AttendanceStatus> attendance;

    private final Set<ClassPostDto> posts;
    private final Set<ClassHomeworkDto> homework;
    private final Set<ResourceDto> resources;
    
    public LessonDto(Lesson lesson) {
        this.id = lesson.getId();
        this.attendance = lesson.getAttendance().entrySet().stream().collect(Collectors.toMap((k) ->
          k.getKey().getDisplayName(), Map.Entry::getValue));

        this.posts = lesson.getPosts().stream().map(ClassPost::getDto).collect(Collectors.toSet());
        this.homework = lesson.getHomework().stream().map(ClassHomework::getDto).collect(Collectors.toSet());
        this.resources = lesson.getResources().stream().map(Resource::getDto).collect(Collectors.toSet());
    }
}
