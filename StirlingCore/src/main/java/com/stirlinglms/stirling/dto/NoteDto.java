package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.note.Note;
import com.stirlinglms.stirling.entity.res.Resource;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class NoteDto {

    private final long id;
    private final UserDto owner;

    private final String title;
    private final String content;
    private final Set<ResourceDto> resources;

    private final Instant createdOn;
    private final Instant editedOn;
    
    public NoteDto(Note note) {
        this.id = note.getId();
        this.owner = note.getOwner().getDto();

        this.title = note.getTitle();
        this.content = note.getContent();
        this.resources = note.getResources().stream().map(Resource::getDto).collect(Collectors.toSet());

        this.createdOn = note.getCreatedOn();
        this.editedOn = note.getEditedOn();
    }
}
