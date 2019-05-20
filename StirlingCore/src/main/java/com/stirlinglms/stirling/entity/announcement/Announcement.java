package com.stirlinglms.stirling.entity.announcement;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.announcement.AnnouncementDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import com.stirlinglms.stirling.repository.AnnouncementRepository;
import com.stirlinglms.stirling.service.AnnouncementService;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Immutable
@Entity(name = "announcements")
public class Announcement implements SaveableEntity<Announcement, AnnouncementDto, AnnouncementService, AnnouncementRepository> {

    @Id
    private Long id;

    private String title;

    @Reference
    private User poster;

    private String content;
    private Instant postTime;
    private Instant editTime;

    @Reference
    private Set<Resource> attachments;
    private Set<UserType> audiences;
    private Set<String> tags;

    @Deprecated
    private Announcement() {}

    public Announcement(String title, User poster, String content, @Nullable Set<Resource> attachments,
                        @Nullable Set<UserType> audiences, @Nullable Set<String> tags) {
        this.title = title;

        this.poster = poster;

        this.content = content;
        this.postTime = Instant.now();
        this.editTime = Instant.now();

        this.attachments = attachments == null ? Sets.newConcurrentHashSet() : attachments;
        this.audiences = audiences == null ? Stream.of(UserType.values()).collect(Collectors.toSet()) : audiences;
        this.tags = tags == null ? Sets.newConcurrentHashSet() : tags;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public User getPoster() {
        return this.poster;
    }

    public String getContent() {
        return this.content;
    }

    public Instant getPostTime() {
        return this.postTime;
    }

    public Instant getEditTime() {
        return this.editTime;
    }

    public Set<Resource> getAttachments() {
        return this.attachments;
    }

    public Set<UserType> getAudiences() {
        return this.audiences;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEditTime(Instant editTime) {
        this.editTime = editTime;
    }

    public void setAttachments(Set<Resource> attachments) {
        this.attachments = attachments;
    }

    public void setAudiences(Set<UserType> audiences) {
        this.audiences = audiences;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public AnnouncementService getService() {
        return Stirling.get().getAnnouncementService();
    }

    @Override
    public AnnouncementDto getDto() {
        return new AnnouncementDto(this);
    }
}
