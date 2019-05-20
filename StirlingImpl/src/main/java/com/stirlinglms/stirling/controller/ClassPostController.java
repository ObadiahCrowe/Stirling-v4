package com.stirlinglms.stirling.controller;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.dto.ClassPostDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.classroom.Classroom;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.service.ClassPostService;
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
public class ClassPostController {

    private final ClassPostService postService;
    private final ClassroomService classroomService;
    private final UserService userService;

    @Autowired
    ClassPostController(ClassPostService postService, ClassroomService classroomService, UserService userService) {
        this.postService = postService;
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @PostMapping(value = "/v1/classroom/{id}/post")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public Response<ClassPostDto> postClassroomPost(
      @PathVariable("id") long id,
      @RequestParam("title") String title,
      @RequestParam("content") String content,
      @RequestParam(value = "resources", required = false) Set<Resource> resources) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            ClassPost post = this.postService.createPost(user, title, content, resources == null ? Sets.newConcurrentHashSet() : resources);
            Set<ClassPost> posts = Sets.newConcurrentHashSet(classroom.getPosts());
            posts.add(post);

            try {
                //this.classroomService.update(classroom, "posts", posts, UpdateLevel.SYSTEM);
            } catch (Exception e) {
                return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Post posted.", post.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{id}/post/{postId}")
    public Response<ClassPostDto> getClassroomPost(
      @PathVariable("id") long id,
      @PathVariable("postId") long postId) {
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

            return new Response<>(ResponseCode.SUCCESS, "Post found.", this.postService.getById(postId).getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/classroom/{id}/post/all")
    public Response<Set<ClassPostDto>> getClassroomPostAll(@PathVariable("id") long id) {
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

            return new Response<>(ResponseCode.SUCCESS, "Posts found.",
              this.postService.getByClass(classroom).stream().map(ClassPost::getDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/classroom/{id}/post/{postId}")
    @PreAuthorize(value = "hasRole('TEACHER')")
    public Response<ClassPostDto> deleteClassroomPost(
      @PathVariable("id") long id,
      @PathVariable("postId") long postId) {
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

            ClassPost post = this.postService.getById(postId);

            if (post == null) {
                throw new NullPointerException("Post does not exist.");
            }

            return new Response<>(ResponseCode.SUCCESS, "Post deleted.", this.postService.delete(post).getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/classroom/{id}/post/{postId}")
    @PreAuthorize(value = "hasRole('TEACHER')")
    public Response<ClassPostDto> patchClassPost(
      @PathVariable("id") long id,
      @PathVariable("postId") long postId,
      @RequestParam("field") String field,
      @RequestParam("value") Object value) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());
            Classroom classroom = this.classroomService.getById(id);

            if (classroom == null) {
                return new Response<>(ResponseCode.ERROR, "Classroom does not exist.", null);
            }

            if (classroom.getTeachers().stream().noneMatch(u -> u.getId() == user.getId())) {
                throw new IllegalAccessException("You are not a teacher of this class. Contact the class teacher for access.");
            }

            ClassPost post = this.postService.getById(postId);

            if (post == null) {
                throw new NullPointerException("Post does not exist.");
            }

            //this.postService.update(post, field, value, UpdateLevel.ADMIN).getDto()
            return new Response<>(ResponseCode.SUCCESS, "Post updated.", null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
