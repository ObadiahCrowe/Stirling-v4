package com.stirlinglms.stirling.entity.note;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.NoteDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.NoteRepository;
import com.stirlinglms.stirling.service.NoteService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Entity(name = "notes")
public class Note implements SaveableEntity<Note, NoteDto, NoteService, NoteRepository> {

    @Id
    private Long id;

    @Reference
    private User owner;

    private String title;
    private String content;

    @Reference
    private Set<Resource> resources;

    private Instant createdOn;
    private Instant editedOn;
    
    @Deprecated
    private Note() {}
    
    public Note(User owner, String title) {
        this.owner = owner;

        this.title = title;
        this.content = "";
        this.resources = Sets.newConcurrentHashSet();

        this.createdOn = Instant.now();
        this.editedOn = Instant.now();
    }

    public Note(User owner, String title, String content) {
        this.owner = owner;

        this.title = title;
        this.content = content;
        this.resources = Sets.newConcurrentHashSet();

        this.createdOn = Instant.now();
        this.editedOn = Instant.now();
    }

    public Long getId() {
        return this.id;
    }

    public User getOwner() {
        return this.owner;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Instant getEditedOn() {
        return this.editedOn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public void setEditedOn(Instant editedOn) {
        this.editedOn = editedOn;
    }

    @Override
    public NoteService getService() {
        return Stirling.get().getNoteService();
    }

    @Override
    public NoteDto getDto() {
        return new NoteDto(this);
    }
}
