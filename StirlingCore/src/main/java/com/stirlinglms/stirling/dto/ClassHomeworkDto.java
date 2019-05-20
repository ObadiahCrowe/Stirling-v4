package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.classroom.info.ClassHomework;
import com.stirlinglms.stirling.entity.res.Resource;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class ClassHomeworkDto {

    private final long id;
    private final UserDto poster;

    private final String title;
    private final String content;

    private final Instant createdOn;
    private final Instant editedOn;
    private final Instant dueOn;

    private final Set<ResourceDto> resources;
    
    public ClassHomeworkDto(ClassHomework homework) {
        this.id = homework.getId();
        this.poster = homework.getPoster().getDto();

        this.title = homework.getTitle();
        this.content = homework.getContent();

        this.createdOn = homework.getCreatedOn();
        this.editedOn = homework.getEditedOn();
        this.dueOn = homework.getDueOn();

        this.resources = homework.getResources().stream().map(Resource::getDto).collect(Collectors.toSet());
    }
}
