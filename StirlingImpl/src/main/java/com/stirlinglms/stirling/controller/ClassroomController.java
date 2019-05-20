package com.stirlinglms.stirling.controller;

import com.stirlinglms.stirling.dto.ClassroomDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.service.*;
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
public class ClassroomController {

    private final ClassroomService classroomService;
    private final UserService userService;
    private final SchoolService schoolService;

    private final AssignmentService assignmentService;
    private final AssignmentUserDataService userDataService;
    private final ClassHomeworkService homeworkService;
    private final ClassPostService classPostService;
    private final ClassSectionService classSectionService;
    private final LessonService lessonService;

    @Autowired
    ClassroomController(ClassroomService classroomService, UserService userService, SchoolService schoolService, AssignmentService assignmentService,
                        AssignmentUserDataService userDataService, ClassHomeworkService homeworkService, ClassPostService classPostService,
                        ClassSectionService classSectionService, LessonService lessonService) {
        this.classroomService = classroomService;
        this.userService = userService;
        this.schoolService = schoolService;

        this.assignmentService = assignmentService;
        this.userDataService = userDataService;
        this.homeworkService = homeworkService;
        this.classPostService = classPostService;
        this.classSectionService = classSectionService;
        this.lessonService = lessonService;
    }

    @PostMapping(value = "/v1/classroom")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<ClassroomDto> postClassroom(
      @RequestParam("name") String name,
      @RequestParam("room") String room,
      @RequestParam("timeSlot") short timeSlotId,
      @RequestParam("school") String schoolName) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            School school = this.schoolService.getByName(schoolName);

            if (school == null) {
                throw new NullPointerException("The school, " + schoolName + ", does not exist.");
            }

            TimeSlot slot = school.getTimeSlots().stream().filter(t -> t.getId() == timeSlotId).findFirst().orElse(null);

            if (slot == null) {
                throw new NullPointerException("There is no TimeSlot with the id: " + timeSlotId + ".");
            }

            return new Response<>(ResponseCode.SUCCESS, "Class posted.", this.classroomService.createClassroom(user, name, room, slot, school).getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/all")
    public Response<Set<ClassroomDto>> getClassroomAll() {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            return new Response<>(ResponseCode.SUCCESS, "Classrooms found.",
              this.classroomService.getAll().stream().filter(c -> c.getStudents().contains(user)).map(Classroom::getDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{id}")
    public Response<ClassroomDto> getClassroom(@PathVariable("id") long id) {
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

            return new Response<>(ResponseCode.SUCCESS, "Classroom found.", classroom.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/classroom/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<ClassroomDto> deleteClassroom(@PathVariable("id") long id) {
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

            classroom.getAssignments().forEach(a -> {
                a.getAssignmentUsers().forEach(this.userDataService::delete);
                this.assignmentService.delete(a);
            });

            classroom.getHomework().forEach(this.homeworkService::delete);
            classroom.getPosts().forEach(this.classPostService::delete);
            classroom.getSections().forEach(this.classSectionService::delete);
            classroom.getLessons().forEach(this.lessonService::delete);

            return new Response<>(ResponseCode.SUCCESS, "Classroom deleted.", this.classroomService.delete(classroom).getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/classroom/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<ClassroomDto> patchClassroom(
      @PathVariable("id") long id,
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

            //this.classroomService.update(classroom, field, value, UpdateLevel.USER).getDto()
            return new Response<>(ResponseCode.SUCCESS, "Classroom updated.", null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
