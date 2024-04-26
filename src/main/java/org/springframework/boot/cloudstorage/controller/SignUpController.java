package org.springframework.boot.cloudstorage.controller;

import org.springframework.boot.cloudstorage.model.User;
import org.springframework.boot.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signup() {
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        if (userService.isUserAvailable(user.getUserName())) {
            userService.createUser(user);
            model.addAttribute("signupSuccess", true);
            redirectAttributes.addFlashAttribute("signupSuccess", "You successfully signed up!");

            return "redirect:/login";
        } else {
            model.addAttribute("signupError", "This username has already existed!");

            return "signup";
        }
    }
}
