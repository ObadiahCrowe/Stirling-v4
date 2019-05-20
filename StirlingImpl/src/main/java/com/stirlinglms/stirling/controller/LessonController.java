package com.stirlinglms.stirling.controller;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.dto.LessonDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.lesson.Lesson;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.service.ClassroomService;
import com.stirlinglms.stirling.service.LessonService;
import com.stirlinglms.stirling.service.UserService;
import com.stirlinglms.stirling.util.UpdateLevel;
import com.stirlinglms.stirling.util.response.Response;
import com.stirlinglms.stirling.util.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class LessonController {

    private final LessonService lessonService;
    private final ClassroomService classroomService;
    private final UserService userService;

    @Autowired
    LessonController(LessonService lessonService, ClassroomService classroomService, UserService userService) {
        this.lessonService = lessonService;
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @PostMapping(value = "/v1/classroom/{classId}/lesson")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<LessonDto> postLesson(
      @PathVariable("classId") long classId,
      @RequestParam("timeSlotId") short timeSlotId) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(classId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (classroom.getTeachers().stream().noneMatch(u -> u.getId().equals(user.getId()))) {
                return new Response<>(ResponseCode.ERROR, "You are not a teacher of this class. You cannot access this resource.", null);
            }

            TimeSlot timeSlot = classroom.getSchool().getTimeSlots().stream().filter(t -> t.getId() == timeSlotId).findFirst().orElse(null);

            if (timeSlot == null) {
                return new Response<>(ResponseCode.ERROR, "TimeSlot not found.", null);
            }

            Lesson lesson = this.lessonService.createLesson(classroom, timeSlot);
            Set<Lesson> lessons = Sets.newConcurrentHashSet(classroom.getLessons());
            lessons.add(lesson);

            classroom.getLessons().addAll(lessons);

            this.classroomService.update(classroom, UpdateLevel.SYSTEM);
            return new Response<>(ResponseCode.SUCCESS, "Lesson created.", lesson.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{classId}/lesson/{id}")
    public Response<LessonDto> getLesson(@PathVariable("classId") long classId, @PathVariable("id") long id) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(classId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (Stream.concat(classroom.getTeachers().stream(), classroom.getStudents().stream()).noneMatch(u -> u.getId().equals(user.getId()))) {
                return new Response<>(ResponseCode.ERROR, "You are not a member of this class. You cannot access this resource.", null);
            }

            Lesson lesson = this.lessonService.getById(id);
            if (lesson == null) {
                return new Response<>(ResponseCode.ERROR, "Lesson does not exist.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Lesson found.", lesson.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{classId}/lesson/all")
    public Response<Set<LessonDto>> getLessonAll(@PathVariable("classId") long classId) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(classId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (Stream.concat(classroom.getTeachers().stream(), classroom.getStudents().stream()).noneMatch(u -> u.getId().equals(user.getId()))) {
                return new Response<>(ResponseCode.ERROR, "You are not a member of this class. You cannot access this resource.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Lessons found.", classroom.getLessons().stream().map(Lesson::getDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/classroom/{classId}/lesson/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<LessonDto> deleteLesson(@PathVariable("classId") long classId, @PathVariable("id") long id) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(classId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (classroom.getTeachers().stream().noneMatch(u -> u.getId().equals(user.getId()))) {
                return new Response<>(ResponseCode.ERROR, "You are not a teacher of this class. You cannot access this resource.", null);
            }

            Lesson lesson = this.lessonService.getById(id);
            if (lesson == null) {
                return new Response<>(ResponseCode.ERROR, "Lesson does not exist.", null);
            }

            classroom.setLessons(classroom.getLessons().stream().filter(l -> !l.getId().equals(lesson.getId())).collect(Collectors.toSet()));

            this.classroomService.update(classroom, UpdateLevel.SYSTEM);
            return new Response<>(ResponseCode.SUCCESS, "Lesson deleted.", this.lessonService.delete(lesson).getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/classroom/{classId}/lesson/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<LessonDto> patchLesson(
      @PathVariable("classId") long classId,
      @PathVariable("id") long id,
      @RequestParam("field") String field,
      @RequestParam("value") Object value) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(classId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (classroom.getTeachers().stream().noneMatch(u -> u.getId().equals(user.getId()))) {
                return new Response<>(ResponseCode.ERROR, "You are not a teacher of this class. You cannot access this resource.", null);
            }

            Lesson lesson = this.lessonService.getById(classId);
            if (lesson == null) {
                return new Response<>(ResponseCode.ERROR, "Lesson does not exist.", null);
            }

            //this.lessonService.update(lesson, field, value, UpdateLevel.USER).getDto()
            return new Response<>(ResponseCode.SUCCESS, "Lesson deleted.", null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
