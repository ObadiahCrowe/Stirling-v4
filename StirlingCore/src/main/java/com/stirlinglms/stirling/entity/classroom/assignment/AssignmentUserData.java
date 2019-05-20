package com.stirlinglms.stirling.entity.classroom.assignment;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.AssignmentUserDataDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.AssignmentUserDataRepository;
import com.stirlinglms.stirling.service.AssignmentUserDataService;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Immutable
@Entity(name = "assignmentusers")
public class AssignmentUserData implements SaveableEntity<AssignmentUserData, AssignmentUserDataDto, AssignmentUserDataService, AssignmentUserDataRepository> {

    @Id
    private Long id;

    @Reference
    private User user;

    private UUID uuid;
    private Result result;

    @Reference
    private Set<Resource> submittedResources;

    private Instant assignedOn;
    private Instant completedOn;

    @Deprecated
    private AssignmentUserData() {}

    public AssignmentUserData(User user) {
        this.user = user;

        this.uuid = UUID.randomUUID();
        this.result = null;
        this.submittedResources = Sets.newConcurrentHashSet();

        this.assignedOn = Instant.now();
        this.completedOn = Instant.MAX;
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Result getResult() {
        return this.result;
    }

    public Set<Resource> getSubmittedResources() {
        return this.submittedResources;
    }

    public Instant getAssignedOn() {
        return this.assignedOn;
    }

    public Instant getCompletedOn() {
        return this.completedOn;
    }

    @Override
    public AssignmentUserDataService getService() {
        return Stirling.get().getAssignmentUserDataService();
    }

    @Override
    public AssignmentUserDataDto getDto() {
        return new AssignmentUserDataDto(this);
    }
}
