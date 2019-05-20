package com.stirlinglms.stirling.controller;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.dto.assignment.AssignmentBaseDto;
import com.stirlinglms.stirling.dto.assignment.AssignmentStudentDto;
import com.stirlinglms.stirling.dto.assignment.AssignmentTeacherDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import com.stirlinglms.stirling.service.AssignmentService;
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

@RestController
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final ClassroomService classroomService;
    private final UserService userService;

    @Autowired
    AssignmentController(AssignmentService assignmentService, ClassroomService classroomService, UserService userService) {
        this.assignmentService = assignmentService;
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @PostMapping(value = "/v1/classroom/{id}/assignment")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<AssignmentTeacherDto> postClassroomAssignment(
      @PathVariable("id") long id,
      @RequestParam("title") String title,
      @RequestParam("description") String desc,
      @RequestParam("formative") boolean formative,
      @RequestParam("dueOn") String dueOn) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);

            if (user.getType().getLevel() < UserType.ADMIN.getLevel() &&
              classroom.getTeachers().stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null) == null) {
                throw new IllegalAccessException("You are not a teacher of this class. Contact the class teacher for access.");
            }

            Assignment assignment = this.assignmentService.createAssignment(user, title, desc, formative, Instant.parse(dueOn));
            Set<Assignment> assignments = Sets.newConcurrentHashSet(classroom.getAssignments());
            assignments.add(assignment);

            //this.classroomService.update(classroom, "assignments", assignments, UpdateLevel.SYSTEM);

            return new Response<>(ResponseCode.SUCCESS, "Assignment created.", new AssignmentTeacherDto(assignment));
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{id}/assignment/{assignmentId}")
    public Response<? extends AssignmentBaseDto> getClassroomAssignment(
      @PathVariable("id") long id,
      @PathVariable("assignmentId") long assignmentId) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);
            Assignment assignment = this.assignmentService.getById(assignmentId);
            boolean isTeacher = false;

            if (user.getType().getLevel() < UserType.ADMIN.getLevel()) {
                isTeacher = classroom.getTeachers().stream().anyMatch(u -> u.getId().equals(user.getId()));

                if (!isTeacher && classroom.getStudents().stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null) == null) {
                    throw new IllegalAccessException("You are not a member of this class. Contact the class teacher for access.");
                }
            }

            return new Response<>(ResponseCode.SUCCESS, "Assignment found.", isTeacher ?
              new AssignmentTeacherDto(assignment) :
              new AssignmentStudentDto(assignment, user));
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{id}/assignment/all")
    public Response<Set<? extends AssignmentBaseDto>> getClassroomAssignmentAll(@PathVariable("id") long id) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);
            boolean isTeacher = false;

            if (user.getType().getLevel() < UserType.ADMIN.getLevel()) {
                isTeacher = (classroom.getTeachers().stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null) == null);

                if (!isTeacher && classroom.getStudents().stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null) == null) {
                    throw new IllegalAccessException("You are not a member of this class. Contact the class teacher for access.");
                }
            }

            boolean finalIsTeacher = isTeacher;
            Set<? extends AssignmentBaseDto> assignments = classroom.getAssignments().stream()
              .map(a -> finalIsTeacher ? new AssignmentTeacherDto(a) : new AssignmentStudentDto(a, user))
              .collect(Collectors.toSet());

            return new Response<>(ResponseCode.SUCCESS, "Assignments found.", assignments);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/classroom/{id}/assignment/{assignmentId}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<AssignmentTeacherDto> deleteClassroomAssignment(
      @PathVariable("id") long id,
      @PathVariable("assignmentId") long assignmentId) {
        try {
            Classroom classroom = this.classroomService.getById(id);
            Assignment assignment = this.assignmentService.getById(assignmentId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (assignment == null) {
                return new Response<>(ResponseCode.ERROR, "Assignment does not exist.", null);
            }

            Set<Assignment> assignments = Sets.newConcurrentHashSet(classroom.getAssignments()).stream()
              .filter(a -> !a.getId().equals(assignment.getId())).collect(Collectors.toSet());

            //this.classroomService.update(classroom, "assignments", assignments, UpdateLevel.SYSTEM);

            return new Response<>(ResponseCode.SUCCESS, "Assignment deleted.",
              new AssignmentTeacherDto(this.assignmentService.delete(assignment)));
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/classroom/{id}/assignment/{assignmentId}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<AssignmentTeacherDto> patchClassroomAssignment(
      @PathVariable("id") long id,
      @PathVariable("assignmentId") long assignmentId,
      @RequestParam("field") String field,
      @RequestParam("value") Object value) {
        try {
            Classroom classroom = this.classroomService.getById(id);
            Assignment assignment = this.assignmentService.getById(assignmentId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (assignment == null) {
                return new Response<>(ResponseCode.ERROR, "Assignment does not exist.", null);
            }

            //new AssignmentTeacherDto(this.assignmentService.update(assignment, field, value, UpdateLevel.ADMIN))
            return new Response<>(ResponseCode.SUCCESS, "Assignment updated.", null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
