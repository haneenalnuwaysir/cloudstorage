package org.springframework.boot.cloudstorage.services;

import org.springframework.boot.cloudstorage.mapper.NoteMapper;
import org.springframework.boot.cloudstorage.mapper.UserMapper;
import org.springframework.boot.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final UserService userService;
    private final NoteMapper noteMapper;


    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public int createNote(Note note) {
//        Integer id = userService.getCurrentUserId();
        return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), userService.getCurrentUserId()));
    }

    public List<Note> getNotes() {
        return noteMapper.getNotes(userService.getCurrentUserId());
    }

    public Note getNoteById(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public int editNote(Note note) {
        return noteMapper.edit(new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), userService.getCurrentUserId()));
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }
}
