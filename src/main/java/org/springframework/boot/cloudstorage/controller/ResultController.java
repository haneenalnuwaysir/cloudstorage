package org.springframework.boot.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResultController {

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
