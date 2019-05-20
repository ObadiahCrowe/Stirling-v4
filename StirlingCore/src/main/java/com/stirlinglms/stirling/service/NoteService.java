package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.note.Note;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.NoteRepository;

import java.util.Set;

public interface NoteService extends RepositoryService<Note, NoteRepository> {

    Note createNote(User owner, String title);

    Note createNote(User owner, String title, String content);

    Set<Note> getByOwner(User user);
}
