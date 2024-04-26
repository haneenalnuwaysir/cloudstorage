package org.springframework.boot.cloudstorage.controller;

import org.springframework.boot.cloudstorage.dto.CredentialForm;
import org.springframework.boot.cloudstorage.dto.FileForm;
import org.springframework.boot.cloudstorage.dto.NoteForm;
import org.springframework.boot.cloudstorage.model.File;
import org.springframework.boot.cloudstorage.model.User;
import org.springframework.boot.cloudstorage.services.EncryptionService;
import org.springframework.boot.cloudstorage.services.FileService;
import org.springframework.boot.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/home/upload-file")
    public String handleFileUpload(@RequestParam("uploadFile") MultipartFile fileUpload, Model model, RedirectAttributes redirectAttributes) throws IOException, SQLException {
        String uploadError = null;

        if (fileService.isFileNameExist(fileUpload.getOriginalFilename())) {
            uploadError = "Can not upload a file with same name.";
        }

        if (uploadError != null) {
            model.addAttribute("uploadError", uploadError);
        } else {
            int rowAdded = fileService.createFile(fileUpload);

            if (rowAdded > 0) {
                model.addAttribute("uploadSuccess", true);
                redirectAttributes.addFlashAttribute("successMessage", true);
            }
        }

        model.addAttribute("files", this.fileService.getFiles());

        return "redirect:/result";
    }

    @GetMapping("/home/delete-file/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Model model, RedirectAttributes redirectAttributes) {
        fileService.deleteFile(fileId);
        model.addAttribute("files", this.fileService.getFiles());
        redirectAttributes.addFlashAttribute("successMessage", true);

        return "redirect:/result";
    }

    @GetMapping("/home/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId, Model model) {
        File file = fileService.getFileById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }
}
