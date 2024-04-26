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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;


@Controller
public class CredentialController {
    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/home/add-credential")
    public String addCredential(@ModelAttribute Credential credential, Model model, RedirectAttributes redirectAttributes) {
        if (credential.getCredentialId() != null) {
            credentialService.editCredential(credential);
        } else {
            credentialService.createCredential(credential);
        }

        model.addAttribute("credentials", credentialService.getCredentials());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }

    @GetMapping("/home/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Model model, RedirectAttributes redirectAttributes) {
        credentialService.deleteCredential(credentialId);
        model.addAttribute("credentials", this.credentialService.getCredentials());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }
}
