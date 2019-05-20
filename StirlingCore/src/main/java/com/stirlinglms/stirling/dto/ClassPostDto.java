package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.classroom.info.ClassPost;
import com.stirlinglms.stirling.entity.res.Resource;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class ClassPostDto {

    private final long id;
    private final UserDto poster;

    private final String title;
    private final String content;

    private final Instant createdOn;
    private final Instant editedOn;

    private final Set<ResourceDto> resources;
    
    public ClassPostDto(ClassPost post) {
        this.id = post.getId();
        this.poster = post.getPoster().getDto();

        this.title = post.getTitle();
        this.content = post.getContent();

        this.createdOn = post.getCreatedOn();
        this.editedOn = post.getEditedOn();

        this.resources = post.getResources().stream().map(Resource::getDto).collect(Collectors.toSet());
    }
}
