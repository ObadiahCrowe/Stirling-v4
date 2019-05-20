package com.stirlinglms.stirling.dto.announcement;

import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.announcement.Announcement;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class AnnouncementDto {

    private final long id;
    private final String title;
    private final AnnouncementPosterDto poster;
    private final String content;

    private final Instant postTime;
    private final Instant editTime;

    private final Set<ResourceDto> attachments;
    private final Set<UserType> audiences;
    private final Set<String> tags;

    public AnnouncementDto(Announcement announcement) {
        this.id = announcement.getId();
        this.title = announcement.getTitle();
        this.poster = new AnnouncementPosterDto(announcement.getPoster().getDto());
        this.content = announcement.getContent();

        this.postTime = announcement.getPostTime();
        this.editTime = announcement.getEditTime();

        this.attachments = announcement.getAttachments().stream().map(Resource::getDto).collect(Collectors.toSet());
        this.audiences = announcement.getAudiences();
        this.tags = announcement.getTags();
    }
}
