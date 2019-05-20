package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.note.Note;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface NoteRepository extends DatastoreRepository<Note, Long> {

    List<Note> findAllByOwner(User user);
}
