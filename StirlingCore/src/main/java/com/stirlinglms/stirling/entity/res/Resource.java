package com.stirlinglms.stirling.entity.res;

import com.google.cloud.storage.BlobId;
import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.resource.ResourceDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ResourceRepository;
import com.stirlinglms.stirling.service.ResourceService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Set;

@Entity(name = "resources")
public class Resource implements SaveableEntity<Resource, ResourceDto, ResourceService, ResourceRepository> {

    @Id
    private Long id;

    @Reference
    private User owner;

    private String name;
    private BlobId blobId;
    private Instant createdOn;

    @Reference
    private Set<User> sharedWith;

    @Deprecated
    private Resource() {}

    public Resource(User owner, String name, BlobId blobId) {
        this.owner = owner;

        this.name = name;
        this.blobId = blobId;
        this.createdOn = Instant.now();
        this.sharedWith = Sets.newConcurrentHashSet();
    }

    public Long getId() {
        return this.id;
    }

    public User getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public BlobId getBlobId() {
        return this.blobId;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Set<User> getSharedWith() {
        return this.sharedWith;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSharedWith(Set<User> sharedWith) {
        this.sharedWith = sharedWith;
    }

    @Override
    public ResourceService getService() {
        return Stirling.get().getResourceService();
    }

    @Override
    public ResourceDto getDto() {
        return new ResourceDto(this);
    }
}
