package com.example.backend.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping({"/login", "/registration", "/admin/messages",
    "/humanTable", "/coordinateTable", "/carTable", "/importFile"})
    public String mainPage() {
        return "forward:/index.html";
    }

    @GetMapping({"/error", "/error/**"})
    public String errorPage() {
        return "forward:/index.html";
    }
}
