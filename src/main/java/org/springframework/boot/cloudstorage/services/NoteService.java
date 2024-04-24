package org.springframework.boot.cloudstorage.services;

import org.springframework.boot.cloudstorage.mapper.NoteMapper;
import org.springframework.boot.cloudstorage.mapper.UserMapper;
import org.springframework.boot.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private final UserMapper userMapper;
    private final NoteMapper noteMapper;

    public NoteService(UserMapper userMapper, NoteMapper noteMapper) {
        this.userMapper = userMapper;
        this.noteMapper = noteMapper;
    }

    public void addNote(String title, String description, String userName) {
        Integer id = userMapper.getUsername(userName).getUserId();
        Note note = new Note(0, title, description, id);
        noteMapper.insert(note);
    }

    public Note[] getNoteLists(Integer userId) {
        return noteMapper.getNotesForUser(userId);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public void editNote(Integer noteId, String title, String description) {
        noteMapper.editNote(noteId, title, description);
    }
}
