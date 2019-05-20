package com.stirlinglms.stirling.dto.assignment;

import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.res.Resource;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public class AssignmentBaseDto {

    private final long id;
    private final String title;
    private final String description;
    private final Set<ResourceDto> attachedResources;
    private final boolean formative;

    private final Instant createdOn;
    private final Instant editedOn;
    private final Instant dueOn;

    public AssignmentBaseDto(Assignment assignment) {
        this.id = assignment.getId();
        this.title = assignment.getTitle();
        this.description = assignment.getDesc();
        this.attachedResources = assignment.getAttachedResources().stream().map(Resource::getDto).collect(Collectors.toSet());
        this.formative = assignment.isFormative();

        this.createdOn = assignment.getCreatedOn();
        this.editedOn = assignment.getEditedOn();
        this.dueOn = assignment.getDueOn();
    }
}
