package com.stirlinglms.stirling.controller;

import com.stirlinglms.stirling.dto.NoteDto;
import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.note.Note;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.service.NoteService;
import com.stirlinglms.stirling.service.UserService;
import com.stirlinglms.stirling.util.response.Response;
import com.stirlinglms.stirling.util.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping(value = "/v1/note")
    public Response<NoteDto> postNote(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            if (user == null) {
                return new Response<>(ResponseCode.ERROR, "User not found.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Note created.", this.noteService.createNote(user, title, content == null ? "" : content).getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/v1/note/{id}")
    public Response<NoteDto> getNote(@PathVariable("id") long id) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            if (user == null) {
                return new Response<>(ResponseCode.ERROR, "User not found.", null);
            }

            Note note = this.noteService.getById(id);

            if (note == null || !note.getOwner().getId().equals(user.getId())) {
                return new Response<>(ResponseCode.ERROR, "Note not found.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Note found.", note.getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/v1/note/all")
    public Response<Set<NoteDto>> getNoteAll() {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            if (user == null) {
                return new Response<>(ResponseCode.ERROR, "User not found.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Notes found.",
              this.noteService.getByOwner(user).stream().map(Note::getDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping(value = "/v1/note/{id}")
    public Response<NoteDto> deleteNote(@PathVariable("id") long id) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            if (user == null) {
                return new Response<>(ResponseCode.ERROR, "User not found.", null);
            }

            Note note = this.noteService.getById(id);

            if (note == null || !note.getOwner().getId().equals(user.getId())) {
                return new Response<>(ResponseCode.ERROR, "Note not found.", null);
            }

            return new Response<>(ResponseCode.SUCCESS, "Note deleted.", this.noteService.delete(note).getDto());
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/v1/note/{id}")
    public Response<NoteDto> patchNote(
      @PathVariable("id") long id,
      @RequestParam("field") String field,
      @RequestParam("value") Object value) {
        try {
            SpringEntity entity = (SpringEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.userService.getByUsername(entity.getUsername());

            if (user == null) {
                return new Response<>(ResponseCode.ERROR, "User not found.", null);
            }

            Note note = this.noteService.getById(id);

            if (note == null || !note.getOwner().getId().equals(user.getId())) {
                return new Response<>(ResponseCode.ERROR, "Note not found.", null);
            }

            //this.noteService.update(note, field, value, UpdateLevel.USER).getDto()
            return new Response<>(ResponseCode.SUCCESS, "Note updated.", null);
        } catch (Exception e) {
            return new Response<>(ResponseCode.ERROR, e.getMessage(), null);
        }
    }
}
