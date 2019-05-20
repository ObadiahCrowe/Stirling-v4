package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.classroom.assignment.AssignmentUserData;
import com.stirlinglms.stirling.entity.res.Resource;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class AssignmentUserDataDto {

    private final long id;
    private final UserDto user;
    private final ResultDto result;
    private final Set<ResourceDto> resources;
    private final Instant assignedOn;
    private final Instant completedOn;
    
    public AssignmentUserDataDto(AssignmentUserData data) {
        this.id = data.getId();
        this.user = data.getUser().getDto();
        this.result = data.getResult().getDto();
        this.resources = data.getSubmittedResources().stream().map(Resource::getDto).collect(Collectors.toSet());
        this.assignedOn = data.getAssignedOn();
        this.completedOn = data.getCompletedOn();
    }
}
