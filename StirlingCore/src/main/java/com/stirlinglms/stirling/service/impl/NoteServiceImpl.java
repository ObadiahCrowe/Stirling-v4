package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.note.Note;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.NoteRepository;
import com.stirlinglms.stirling.service.NoteService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private static final String[] VALID_FIELDS = new String[] {
      "title",
      "content",
      "resources"
    };

    private final NoteRepository repository;

    @Autowired
    NoteServiceImpl(NoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Note createNote(User owner, String title) {
        Note note = new Note(owner, title);

        this.repository.save(note);

        return note;
    }

    @Override
    public Note createNote(User owner, String title, String content) {
        Note note = new Note(owner, title, content);

        this.repository.save(note);

        return note;
    }

    @Override
    public Set<Note> getByOwner(User user) {
        return this.repository.findAllByOwner(user).stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Note update(Note entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public Note delete(Note entity) {
        this.repository.delete(entity);

        return entity;
    }

    @Override
    public Note getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<Note> getAll() {
        return Sets.newConcurrentHashSet(this.repository.findAll());
    }

    @Override
    public NoteRepository getRepository() {
        return this.repository;
    }
}
