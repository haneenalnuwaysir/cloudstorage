package org.springframework.boot.cloudstorage.controller;


import org.springframework.boot.cloudstorage.dto.CredentialForm;
import org.springframework.boot.cloudstorage.dto.FileForm;
import org.springframework.boot.cloudstorage.dto.NoteForm;
import org.springframework.boot.cloudstorage.model.User;
import org.springframework.boot.cloudstorage.services.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class HomeController {
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("files", this.fileService.getFiles());
        model.addAttribute("notes", this.noteService.getNotes());
        model.addAttribute("credentials", this.credentialService.getCredentials());
        model.addAttribute("credentialService", credentialService);
        return "home";
    }
}
