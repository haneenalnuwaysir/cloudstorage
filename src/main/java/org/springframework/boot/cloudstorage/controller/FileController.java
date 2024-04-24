package org.springframework.boot.cloudstorage.controller;

import org.springframework.boot.cloudstorage.dto.CredentialForm;
import org.springframework.boot.cloudstorage.dto.FileForm;
import org.springframework.boot.cloudstorage.dto.NoteForm;
import org.springframework.boot.cloudstorage.model.User;
import org.springframework.boot.cloudstorage.services.EncryptionService;
import org.springframework.boot.cloudstorage.services.FileService;
import org.springframework.boot.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {
    private final FileService fileService;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public FileController(FileService fileService, EncryptionService encryptionService, UserService userService) {
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }
    @GetMapping
    public String home(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        Integer id  = getUserId(authentication);
        model.addAttribute("files", this.fileService.getFileLists(id));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }
    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }
    @PostMapping("/file")
    public String newFile(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) throws IOException {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        Integer id = user.getUserId();
        String[] fileListings = fileService.getFileLists(id);
        MultipartFile multipartFile = newFile.getFile();
        String fileName = multipartFile.getOriginalFilename();
        boolean fileIsDuplicate = false;
        for (String fileListing: fileListings) {
            if (fileListing.equals(fileName)) {
                fileIsDuplicate = true;

                break;
            }
        }
        if (!fileIsDuplicate) {
            fileService.addFile(multipartFile, userName);
            model.addAttribute("result", "success");
        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "You have tried to add a duplicate file.");
            return "result";
        }
        model.addAttribute("files", fileService.getFileLists(id));

        return "result";
    }

    @GetMapping(value = "/get-file/{fileName}",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getFile(@PathVariable String fileName) {
        return fileService.getFile(fileName).getFileData();
    }

    @GetMapping(value = "/delete-file/{fileName}")
    public String deleteFile(
            Authentication authentication,
            @PathVariable String fileName,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        fileService.deleteFile(fileName);
        Integer id = getUserId(authentication);
        model.addAttribute("files", fileService.getFileLists(id));
        model.addAttribute("result", "success");

        return "result";
    }
}
