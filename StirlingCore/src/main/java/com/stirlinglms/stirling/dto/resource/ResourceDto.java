package com.stirlinglms.stirling.dto.resource;

import com.stirlinglms.stirling.entity.res.Resource;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public class ResourceDto {

    private static final String BASE_URL = "https://api.stirlinglms.com/v1/resource/get?=";

    private final long id;
    private final ResourceUserDto owner;

    private final String name;
    private final String downloadUrl;
    private final Instant createdOn;
    private final Set<ResourceUserDto> sharedWith;

    public ResourceDto(Resource resource) {
        this.id = resource.getId();
        this.owner = new ResourceUserDto(resource.getOwner());

        this.name = resource.getName();
        this.downloadUrl = BASE_URL + resource.getBlobId().getGeneration();
        this.createdOn = resource.getCreatedOn();
        this.sharedWith = resource.getSharedWith().stream().map(ResourceUserDto::new).collect(Collectors.toSet());
    }
}
