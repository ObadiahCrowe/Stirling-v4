package com.stirlinglms.stirling.controller;

import com.stirlinglms.stirling.dto.AssignmentUserDataDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.assignment.AssignmentUserData;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import com.stirlinglms.stirling.service.AssignmentService;
import com.stirlinglms.stirling.service.AssignmentUserDataService;
import com.stirlinglms.stirling.service.ClassroomService;
import com.stirlinglms.stirling.service.UserService;
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
public class AssignmentUserDataController {

    private final AssignmentUserDataService userDataService;
    private final AssignmentService assignmentService;
    private final ClassroomService classroomService;
    private final UserService userService;

    @Autowired
    AssignmentUserDataController(AssignmentUserDataService userDataService, AssignmentService assignmentService,
                                 ClassroomService classroomService, UserService userService) {
        this.userDataService = userDataService;
        this.assignmentService = assignmentService;
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @GetMapping(value = "/v1/classroom/{id}/assignment/{assignmentId}/all")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<Set<AssignmentUserDataDto>> getClassroomAssignmentAll(
      @PathVariable("id") long id,
      @PathVariable("assignmentId") long assignmentId) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);
            Assignment assignment = this.assignmentService.getById(assignmentId);

            if (user.getType().getLevel() < UserType.ADMIN.getLevel() && classroom.getTeachers().stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null) == null) {
                throw new IllegalAccessException("You are not a teacher of this class. You cannot access this resource.");
            }

            Set<AssignmentUserDataDto> data = assignment.getAssignmentUsers().stream().filter(a -> a.getId().equals(assignment.getId()))
              .map(AssignmentUserData::getDto).collect(Collectors.toSet());

            return new Response<>(ResponseCode.SUCCESS, "Assignment data found,", data);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{id}/assignment/{assignmentId}/me")
    public Response<AssignmentUserDataDto> getClassroomAssignmentMe(
      @PathVariable("id") long id,
      @PathVariable("assignmentId") long assignmentId) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);
            Assignment assignment = this.assignmentService.getById(assignmentId);

            if (Stream.concat(classroom.getStudents().stream(), classroom.getTeachers().stream()).filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null) == null) {
                throw new IllegalAccessException("You are not a member of this class. Contact the class teacher for access.");
            }

            AssignmentUserData data = assignment.getAssignmentUsers().stream().filter(u -> u.getUser().getId().equals(user.getId())).findFirst().orElse(null);

            if (data == null) {
                return new Response<>(ResponseCode.SUCCESS, "Assignment data found,", this.userDataService.createAssignmentUser(user).getDto());
            }

            return new Response<>(ResponseCode.SUCCESS, "Assignment data found.", data.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/classroom/{id}/assignment/{assignmentId}/me")
    public Response<AssignmentUserDataDto> deleteClassroomAssignment(
      @PathVariable("id") long id,
      @PathVariable("assignmentId") long assignmentId) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);
            Assignment assignment = this.assignmentService.getById(assignmentId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (assignment == null) {
                return new Response<>(ResponseCode.ERROR, "Assignment does not exist.", null);
            }

            AssignmentUserData data = assignment.getAssignmentUsers().stream()
              .filter(a -> a.getUser().getId() == user.getId())
              .findFirst()
              .orElse(null);

            if (data == null) {
                throw new NullPointerException("Assignment data does not exist.");
            }

            this.userDataService.delete(data);
            //this.assignmentService.update(assignment, "assignmentUsers", assignment.getAssignmentUsers().stream()
            // .filter(a -> a.getUser().getId() != user.getId()).collect(Collectors.toSet()), UpdateLevel.SYSTEM);

            return new Response<>(ResponseCode.SUCCESS, "Assignment data deleted.", data.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/classroom/{id}/assignment/{assignmentId}/me")
    public Response<AssignmentUserDataDto> patchClassroomAssignment(
      @PathVariable("id") long id,
      @PathVariable("assignmentId") long assignmentId,
      @RequestParam("field") String field,
      @RequestParam("value") Object value) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);
            Assignment assignment = this.assignmentService.getById(assignmentId);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (assignment == null) {
                return new Response<>(ResponseCode.ERROR, "Assignment does not exist.", null);
            }

            AssignmentUserData data = assignment.getAssignmentUsers().stream()
              .filter(d -> d.getUser().getId() == user.getId())
              .findFirst()
              .orElse(null);

            if (data == null) {
                throw new NullPointerException("Assignment data does not exist.");
            }

            //this.userDataService.update(data, field, value, UpdateLevel.USER).getDto()
            return new Response<>(ResponseCode.SUCCESS, "Assignment data updated.", null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
