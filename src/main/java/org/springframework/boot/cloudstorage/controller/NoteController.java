package org.springframework.boot.cloudstorage.controller;


import org.springframework.boot.cloudstorage.dto.CredentialForm;
import org.springframework.boot.cloudstorage.dto.FileForm;
import org.springframework.boot.cloudstorage.dto.NoteForm;
import org.springframework.boot.cloudstorage.model.Note;
import org.springframework.boot.cloudstorage.model.User;
import org.springframework.boot.cloudstorage.services.NoteService;
import org.springframework.boot.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/home/add-note")
    public String createNote(@ModelAttribute Note note, Model model, RedirectAttributes redirectAttributes) {
        if (note.getNoteId() != null) {
            noteService.editNote(note);
        } else {
            noteService.createNote(note);
        }

        model.addAttribute("notes", noteService.getNotes());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }

    @GetMapping("/home/delete-note/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Model model, RedirectAttributes redirectAttributes) {
        noteService.deleteNote(noteId);
        model.addAttribute("notes", this.noteService.getNotes());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }
}

