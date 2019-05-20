package com.stirlinglms.stirling.controller;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.dto.ClassHomeworkDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.service.ClassHomeworkService;
import com.stirlinglms.stirling.service.ClassroomService;
import com.stirlinglms.stirling.service.UserService;
import com.stirlinglms.stirling.util.response.Response;
import com.stirlinglms.stirling.util.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class ClassHomeworkController {

    private final ClassHomeworkService homeworkService;
    private final ClassroomService classroomService;
    private final UserService userService;

    @Autowired
    ClassHomeworkController(ClassHomeworkService homeworkService, ClassroomService classroomService, UserService userService) {
        this.homeworkService = homeworkService;
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @PostMapping(value = "/v1/classroom/{id}/homework")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<ClassHomeworkDto> postClassroomHomework(
      @PathVariable("id") long id,
      @RequestParam("title") String title,
      @RequestParam("content") String content,
      @RequestParam("dueOn") String dueOn,
      @RequestParam(value = "resources", required = false) Set<Resource> resources) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            Instant instant;

            try {
                instant = Instant.parse(dueOn);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Due date must be a date.");
            }

            ClassHomework homework = this.homeworkService.createHomework(user, title, content, instant,
              resources == null ? Sets.newConcurrentHashSet() : resources);

            Set<ClassHomework> items = Sets.newConcurrentHashSet(classroom.getHomework());
            items.add(homework);

            try {
                //this.classroomService.update(classroom, "homework", items, UpdateLevel.SYSTEM);
            } catch (Exception e) {
                return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Homework posted.", homework.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{id}/homework/{homeworkId}")
    public Response<ClassHomeworkDto> getClassroomHomework(
      @PathVariable("id") long id,
      @PathVariable("homeworkId") long homeworkId) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (Stream.concat(classroom.getStudents().stream(), classroom.getTeachers().stream()).noneMatch(u -> u.getId().equals(user.getId()))) {
                throw new IllegalAccessException("You are not a member of this class. Contact the class teacher for access.");
            }

            return new Response<>(ResponseCode.SUCCESS, "Homework found.", this.homeworkService.getById(homeworkId).getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{id}/homework/all")
    public Response<Set<ClassHomeworkDto>> getClassroomHomeworkAll(@PathVariable("id") long id) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (Stream.concat(classroom.getStudents().stream(), classroom.getTeachers().stream()).noneMatch(u -> u.getId().equals(user.getId()))) {
                throw new IllegalAccessException("You are not a member of this class. Contact the class teacher for access.");
            }

            return new Response<>(ResponseCode.SUCCESS, "Homework found.",
              this.homeworkService.getByClass(classroom).stream().map(ClassHomework::getDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/classroom/{id}/homework/{homeworkId}")
    @PreAuthorize(value = "hasRole('TEACHER')")
    public Response<ClassHomeworkDto> deleteClassroomHomework(
      @PathVariable("id") long id,
      @PathVariable("homeworkId") long homeworkId) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (classroom.getTeachers().stream().noneMatch(u -> u.getId().equals(user.getId()))) {
                throw new IllegalAccessException("You are not a teacher of this class. Contact the class teacher for access.");
            }

            ClassHomework homework = this.homeworkService.getById(homeworkId);

            if (homework == null) {
                throw new NullPointerException("Homework does not exist.");
            }

            return new Response<>(ResponseCode.SUCCESS, "Homework deleted.", this.homeworkService.delete(homework).getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/classroom/{id}/homework/{homeworkId}")
    @PreAuthorize(value = "hasRole('TEACHER')")
    public Response<ClassHomeworkDto> patchClassHomework(
      @PathVariable("id") long id,
      @PathVariable("homeworkId") long homeworkId,
      @RequestParam("field") String field,
      @RequestParam("value") Object value) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (classroom.getTeachers().stream().noneMatch(u -> u.getId().equals(user.getId()))) {
                throw new IllegalAccessException("You are not a teacher of this class. Contact the class teacher for access.");
            }

            ClassHomework homework = this.homeworkService.getById(homeworkId);

            if (homework == null) {
                throw new NullPointerException("Homework does not exist.");
            }

            //this.homeworkService.update(homework, field, value, UpdateLevel.ADMIN).getDto()
            return new Response<>(ResponseCode.SUCCESS, "Homework updated.", null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
