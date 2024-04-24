package org.springframework.boot.cloudstorage.controller;

import org.springframework.boot.cloudstorage.dto.CredentialForm;
import org.springframework.boot.cloudstorage.dto.FileForm;
import org.springframework.boot.cloudstorage.dto.NoteForm;
import org.springframework.boot.cloudstorage.model.Credential;
import org.springframework.boot.cloudstorage.model.User;
import org.springframework.boot.cloudstorage.services.CredentialService;
import org.springframework.boot.cloudstorage.services.EncryptionService;
import org.springframework.boot.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("credential")
public class CredentialController {

    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @GetMapping
    public String home(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            @ModelAttribute("newNote") NoteForm newNote,
            Model model) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("credentials", this.credentialService.getCredentialLists(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    @PostMapping("add-credential")
    public String newCredential(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            @ModelAttribute("newNote") NoteForm newNote,
            Model model) {
        String userName = authentication.getName();
        String newUrl = newCredential.getUrl();
        String stringCredentialId = newCredential.getCredentialId();
        String credentialPassword = newCredential.getPassword();

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialPassword, encodedKey);

        if (stringCredentialId.isEmpty()) {
            credentialService.addCredential(newUrl, userName, newCredential.getUserName(), encodedKey, encryptedPassword);
        } else {
            Credential existingCredential = getCredential(Integer.parseInt(stringCredentialId));
            credentialService.editCredential(existingCredential.getCredentialid(), newCredential.getUserName(), newUrl, encodedKey, encryptedPassword);
        }
        User user = userService.getUser(userName);
        model.addAttribute("credentials", credentialService.getCredentialLists(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping(value = "/get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @GetMapping(value = "/delete-credential/{credentialId}")
    public String deleteCredential(
            Authentication authentication, @PathVariable Integer credentialId,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote, Model model) {
        credentialService.deleteCredential(credentialId);
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("credentials", credentialService.getCredentialLists(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }
}
