package com.stirlinglms.stirling.controller;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.dto.announcement.AnnouncementDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.announcement.Announcement;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import com.stirlinglms.stirling.service.AnnouncementService;
import com.stirlinglms.stirling.service.UserService;
import com.stirlinglms.stirling.util.response.Response;
import com.stirlinglms.stirling.util.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final UserService userService;

    @Autowired
    AnnouncementController(AnnouncementService announcementService, UserService userService) {
        this.announcementService = announcementService;
        this.userService = userService;
    }

    @PostMapping(value = "/v1/announcement")
    @PreAuthorize(value = "hasAuthority('GROUP_LEADER')")
    public Response<AnnouncementDto> postAnnouncement(
      @RequestParam("title") String title,
      @RequestParam("content") String content,
      @RequestParam(value = "audiences", required = false) Set<UserType> audiences,
      @RequestParam(value = "tags", required = false) Set<String> tags) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            Announcement announcement = this.announcementService.createAnnouncement(
              title,
              user,
              content,
              Sets.newConcurrentHashSet(),
              audiences == null ? Sets.newConcurrentHashSet(Arrays.asList(UserType.values())) : audiences,
              tags == null ? Sets.newConcurrentHashSet() : tags);

            return new Response<>(ResponseCode.SUCCESS, "Announcement, " + announcement.getTitle() +
              ", created.", announcement.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/announcement/{id}")
    public Response<AnnouncementDto> getAnnouncement(@PathVariable("id") long id) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            Announcement announcement = this.announcementService.getById(id);

            if (announcement == null || !announcement.getAudiences().contains(user.getType())) {
                return new Response<>(ResponseCode.ERROR, "Announcement does not exist.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Announcement found.", announcement.getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/announcement/all")
    public Response<Set<AnnouncementDto>> getAnnouncementAll() {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            return new Response<>(
              ResponseCode.SUCCESS,
              "Announcements found.",
              this.announcementService.getByAudience(user.getType())
                .stream()
                .map(Announcement::getDto)
                .collect(Collectors.toSet())
            );
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/announcement/{id}")
    @PreAuthorize(value = "hasAuthority('GROUP_LEADER')")
    public Response<AnnouncementDto> deleteAnnouncement(@PathVariable("id") long id) {
        try {
            Announcement announcement = this.announcementService.getById(id);

            if (announcement == null) {
                return new Response<>(ResponseCode.ERROR, "Announcement does not exist.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Announcement deleted.", this.announcementService.delete(announcement).getDto());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/announcement/{id}")
    @PreAuthorize(value = "hasAnyAuthority('GROUP_LEADER')")
    public Response<AnnouncementDto> patchAnnouncement(@PathVariable("id") long id,
                                                       @RequestParam("field") String field,
                                                       @RequestParam("value") Object value) {
        try {
            Announcement announcement = this.announcementService.getById(id);

            if (announcement == null) {
                return new Response<>(ResponseCode.ERROR, "Announcement does not exist.", null);
            }

            //this.announcementService.update(announcement, field, value, UpdateLevel.ADMIN).getDto()
            return new Response<>(ResponseCode.SUCCESS, "Announcement updated.", null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException && e.getMessage().contains("invalid hexadecimal")) {
                return new Response<>(ResponseCode.ERROR, "Invalid id.", null);
            }

            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
